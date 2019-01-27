package cn.wizzer.app.web.commons.slog;

import cn.wizzer.app.sys.modules.models.Sys_log;
import cn.wizzer.app.sys.modules.services.SysLogService;
import cn.wizzer.app.web.commons.utils.StringUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.nutz.Nutz;
import org.nutz.el.El;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.lang.segment.CharSegment;
import org.nutz.lang.util.ClassMetaReader;
import org.nutz.lang.util.Context;
import org.nutz.lang.util.MethodParamNamesScaner;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean(create = "init", depose = "close")
public class SLogService implements Runnable {

    private static final Log log = Logs.get();

    ExecutorService es;

    LinkedBlockingQueue<Sys_log> queue;

    @Inject
    @Reference
    protected SysLogService sysLogService;

    /**
     * 异步插入日志
     *
     * @param syslog 日志对象
     */
    public void async(Sys_log syslog) {
        LinkedBlockingQueue<Sys_log> queue = this.queue;
        if (queue != null)
            try {
                boolean re = queue.offer(syslog, 50, TimeUnit.MILLISECONDS);
                if (!re) {
                    log.info("syslog queue is full, drop it ...");
                }
            } catch (InterruptedException e) {
            }
    }

    /**
     * 同步插入日志
     *
     * @param syslog 日志对象
     */
    public void sync(Sys_log syslog) {
        try {
            sysLogService.fastInsertSysLog(syslog);
        } catch (Throwable e) {
            log.info("insert syslog sync fail", e);
        }
    }

    public void run() {
        while (true) {
            LinkedBlockingQueue<Sys_log> queue = this.queue;
            if (queue == null)
                break;
            try {
                Sys_log sysLog = queue.poll(1, TimeUnit.SECONDS);
                if (sysLog != null) {
                    sync(sysLog);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * 本方法通常由aop拦截器调用.
     *
     * @param t      日志类型
     * @param tag    标签
     * @param source 源码位置
     * @param els    消息模板的EL表达式预处理表
     * @param param  是否异步插入
     * @param result 是否异步插入
     * @param async  是否异步插入
     * @param args   方法参数
     * @param re     方法返回值
     * @param method 方法实例
     * @param obj    被拦截的对象
     * @param e      异常对象
     */
    public void log(String t, String type, String tag, String source, CharSegment seg,
                    Map<String, El> els, boolean param, boolean result,
                    boolean async,
                    Object[] args, Object re, Method method, Object obj,
                    Throwable e) {
        String _msg = null;
        if (seg.hasKey()) {
            Context ctx = Lang.context();
            List<String> names = null;
            if (Nutz.majorVersion() == 1 && Nutz.minorVersion() < 60) {
                Class<?> klass = obj.getClass();
                if (klass.getName().endsWith("$$NUTZAOP"))
                    klass = klass.getSuperclass();
                String key = klass.getName();
                if (caches.containsKey(key))
                    names = caches.get(key).get(ClassMetaReader.getKey(method));
                else {
                    try {
                        Map<String, List<String>> tmp = MethodParamNamesScaner.getParamNames(klass);
                        names = tmp.get(ClassMetaReader.getKey(method));
                        caches.put(key, tmp);
                    } catch (IOException e1) {
                        log.debug("error when reading param name");
                    }
                }
            } else {
                names = MethodParamNamesScaner.getParamNames(method);
            }
            if (names != null) {
                for (int i = 0; i < names.size() && i < args.length; i++) {
                    ctx.set(names.get(i), args[i]);
                }
            }
            ctx.set("obj", obj);
            ctx.set("args", args);
            ctx.set("re", re);
            ctx.set("return", re);
            ctx.set("req", Mvcs.getReq());
            ctx.set("resp", Mvcs.getResp());
            Context _ctx = Lang.context();
            for (String key : seg.keys()) {
                _ctx.set(key, els.get(key).eval(ctx));
            }
            _msg = seg.render(_ctx).toString();
        } else {
            _msg = seg.getOrginalString();
        }
        String _param = "";
        String _result = "";
        if (param && args != null) {
            try {
                _param = Json.toJson(args);
            } catch (Exception e1) {
                _param = "传参不能转换为JSON格式";
            }
        }
        if (result && re != null) {
            try {
                _result = Json.toJson(re);
            } catch (Exception e1) {
                _param = "返回对象不能转换为JSON格式";
            }
        }
        log(type, tag, source, _msg, async, _param, _result);
    }


    public void log(String type, String tag, String source, String msg, boolean async, String param, String result) {
        Sys_log slog = makeLog(type, tag, source, msg, param, result);
        if (async)
            async(slog);
        else
            sync(slog);
    }

    protected static Map<String, Map<String, List<String>>> caches = new HashMap<String, Map<String, List<String>>>();

    public void init() {
        queue = new LinkedBlockingQueue<Sys_log>();
        int c = Runtime.getRuntime().availableProcessors();
        es = Executors.newFixedThreadPool(c);
        for (int i = 0; i < c; i++) {
            es.submit(this);
        }
    }

    public void close() throws InterruptedException {
        queue = null; // 触发关闭
        if (es != null && !es.isShutdown()) {
            es.shutdown();
            es.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public static Sys_log makeLog(String type, String tag, String source, String msg, String param, String result) {
        Sys_log sysLog = new Sys_log();
        if (type == null || tag == null) {
            throw new RuntimeException("type/tag can't null");
        }
        if (source == null) {
            StackTraceElement[] tmp = Thread.currentThread().getStackTrace();
            if (tmp.length > 2) {
                source = tmp[2].getClassName() + "#" + tmp[2].getMethodName();
            } else {
                source = "main";
            }

        }
        sysLog.setType(type);
        sysLog.setTag(tag);
        sysLog.setSrc(source);
        sysLog.setMsg(msg);
        sysLog.setParam(param);
        sysLog.setResult(result);
        if (Mvcs.getReq() != null) {
            sysLog.setIp(Lang.getIP(Mvcs.getReq()));
        }
        sysLog.setOpBy(StringUtil.getPlatformUid());
        sysLog.setOpAt(Times.getTS());
        sysLog.setDelFlag(false);
        sysLog.setUsername(StringUtil.getPlatformUsername());
        return sysLog;
    }
}