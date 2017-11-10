package cn.wizzer.app.web.commons.slog;

import cn.wizzer.app.sys.modules.models.Sys_log;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.el.El;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.lang.reflect.Method;
import java.util.Calendar;
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
    protected Dao dao;
    /**
     * 按月分表的dao实例
     */
    protected Map<String, Dao> ymDaos = new HashMap<String, Dao>();

    /**
     * 获取按月分表的Dao实例,即当前日期的dao实例
     *
     * @return
     */
    public Dao dao() {
        Calendar cal = Calendar.getInstance();
        String key = String.format("%d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        return dao(key);
    }

    /**
     * 获取特定月份的Dao实例
     *
     * @param key
     * @return
     */
    public Dao dao(String key) {
        Dao dao = ymDaos.get(key);
        if (dao == null) {
            synchronized (this) {
                dao = ymDaos.get(key);
                if (dao == null) {
                    dao = Daos.ext(this.dao, key);
                    dao.create(Sys_log.class, false);
                    ymDaos.put(key, dao);
                    try {
                        Daos.migration(dao, Sys_log.class, true, false);
                    } catch (Throwable e) {
                    }
                }
            }
        }
        return dao;
    }

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
            dao().fastInsert(syslog);
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
     * @param t            日志类型
     * @param tag          标签
     * @param source       源码位置
     * @param els          消息模板的EL表达式预处理表
     * @param actionParam  是否异步插入
     * @param methodReturn 是否异步插入
     * @param async        是否异步插入
     * @param args         方法参数
     * @param re           方法返回值
     * @param method       方法实例
     * @param obj          被拦截的对象
     * @param e            异常对象
     */
    public void log(String t, String type, String tag, String source, String msg,
                    Map<String, El> els, boolean param, boolean result,
                    boolean async,
                    Object[] args, Object re, Method method, Object obj,
                    Throwable e) {
        String _param = null;
        String _result = null;
        if (param) {
            _param = Json.toJson(args);
        }
        if (result) {
            _result = Json.toJson(re);
        }
        log(type, tag, source, msg, async, _param, _result);
    }


    public void log(String type, String tag, String source, String msg, boolean async, String param, String result) {
        Sys_log slog = Sys_log.c(type, tag, source, msg, param, result);
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
}