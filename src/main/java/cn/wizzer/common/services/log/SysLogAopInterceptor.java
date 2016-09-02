package cn.wizzer.common.services.log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.el.El;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Lang;
import org.nutz.lang.segment.CharSegment;
import org.nutz.lang.util.Context;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.modules.back.sys.models.Sys_log;

/**
 * Created by wizzer on 2016/6/22.
 */
public class SysLogAopInterceptor implements MethodInterceptor {

    protected SysLogService sysLogService;

    protected String source;

    protected String tag;
    protected CharSegment msg;
    protected boolean before;
    protected boolean after;
    protected boolean error;
    protected boolean async;
    protected Map<String, El> els;
    protected Ioc ioc;

    public SysLogAopInterceptor(Ioc ioc, SLog slog, Method method) {
        this.ioc = ioc;
        this.msg = new CharSegment(slog.msg());
        if (msg.hasKey()) {
            els = new HashMap<String, El>();
            for (String key : msg.keys()) {
                els.put(key, new El(key));
            }
        }
        this.source = method.getDeclaringClass().getName() + "#" + method.getName();
        this.tag = slog.tag();
        SLog _s = method.getDeclaringClass().getAnnotation(SLog.class);
        if (_s != null) {
            this.tag = _s.tag() + "," + this.tag;
        }
        this.async = slog.async();
        this.before = slog.before();
        this.after = slog.after();
        this.error = slog.error();
    }

    public void filter(InterceptorChain chain) throws Throwable {
        if (before)
            doLog("aop.before", chain, null);
        try {
            chain.doChain();
            if (after)
                doLog("aop.after", chain, null);
        } catch (Throwable e) {
            if (error)
                doLog("aop.after", chain, e);
            throw e;
        }
    }

    protected void doLog(String t, InterceptorChain chain, Throwable e) {
        String _msg = null;
        if (msg.hasKey()) {
            Context ctx = Lang.context();
            ctx.set("args", chain.getArgs());
            ctx.set("return", chain.getReturn());
            Context _ctx = Lang.context();
            for (String key : msg.keys()) {
                _ctx.set(key, els.get(key).eval(ctx));
            }
            _msg = msg.render(_ctx).toString();
        } else {
            _msg = msg.getOrginalString();
        }
        Sys_log sysLog = Sys_log.c(t, tag, _msg, source);
        if (sysLogService == null)
            sysLogService = ioc.get(SysLogService.class);
        if (async)
            sysLogService.async(sysLog);
        else
            sysLogService.sync(sysLog);
    }
}