package com.budwk.app.web.commons.slog;

import com.budwk.app.web.commons.slog.annotation.SLog;
import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.el.El;
import org.nutz.ioc.Ioc;
import org.nutz.lang.segment.CharSegment;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/22.
 */
public class SLogAopInterceptor implements MethodInterceptor {
    private static final Log log = Logs.get();

    protected SLogService sLogService;

    protected String source;

    protected String type;
    protected String tag;
    protected CharSegment seg;
    protected boolean param;
    protected boolean result;
    protected boolean async;
    protected Map<String, El> els;
    protected Ioc ioc;

    public SLogAopInterceptor(Ioc ioc, SLog slog, Method method) {
        this.seg = new CharSegment(slog.msg());
        if (seg.hasKey()) {
            els = new HashMap<String, El>();
            for (String key : seg.keys()) {
                els.put(key, new El(key));
            }
        }
        this.param = slog.param();
        this.result = slog.result();
        this.ioc = ioc;
        this.source = method.getDeclaringClass().getName() + "#" + method.getName();
        this.tag = slog.tag();
        SLog _s = method.getDeclaringClass().getAnnotation(SLog.class);
        if (_s != null) {
            this.tag = _s.tag() + "," + this.tag;
        }
        this.type = slog.type();
        this.async = slog.async();
    }

    public void filter(InterceptorChain chain) throws Throwable {
        try {
            chain.doChain();
            doLog("aop.after", seg, chain, null);
        } catch (Throwable e) {
            doLog("aop.error", seg, chain, e);
            throw e;
        }
    }

    protected void doLog(String t, CharSegment seg, InterceptorChain chain, Throwable e) {
        if (sLogService == null)
            sLogService = ioc.get(SLogService.class);
        try {
            sLogService.log(t,
                    type,
                    tag,
                    source,
                    seg,
                    els,
                    param,
                    result,
                    async,
                    chain.getArgs(),
                    chain.getReturn(),
                    chain.getCallingMethod(),
                    chain.getCallingObj(),
                    e);
        } catch (Exception e1) {
            log.debug("slog fail", e1);
        }
    }
}