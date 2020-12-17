package com.budwk.app.web.commons.proc;

import org.nutz.lang.Stopwatch;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.impl.processor.AbstractProcessor;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wizzer.cn on 2015/7/2.
 */
public class LogTimeProcessor extends AbstractProcessor {

    private static final Log log = Logs.get();

    public void process(ActionContext ac) throws Throwable {
        if (log.isDebugEnabled()) {
            Stopwatch sw = Stopwatch.begin();
            try {
                doNext(ac);
            } finally {
                sw.stop();
                HttpServletRequest req = ac.getRequest();
                log.debugf("[%-4s]URI=%s %sms", req.getMethod(), req.getRequestURI(), sw.getDuration());
            }
        } else {
            doNext(ac);
        }
    }
}
