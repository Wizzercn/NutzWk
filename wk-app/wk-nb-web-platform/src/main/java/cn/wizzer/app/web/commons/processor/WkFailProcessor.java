package cn.wizzer.app.web.commons.processor;

import cn.wizzer.framework.base.Result;
import org.nutz.integration.shiro.NutShiro;
import org.nutz.ioc.IocException;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.impl.processor.ViewProcessor;
import org.nutz.mvc.view.ForwardView;

public class WkFailProcessor extends ViewProcessor {

    private static final Log log = Logs.get();
    private String errorUri = "/error/500.html";

    @Override
    public void init(NutConfig config, ActionInfo ai) throws Throwable {
        view = evalView(config, ai, ai.getFailView());
    }

    public void process(ActionContext ac) throws Throwable {
        if (log.isWarnEnabled()) {
            String uri = Mvcs.getRequestPath(ac.getRequest());
            log.warn(String.format("Error@%s :", uri), ac.getError());
        }
        if (ac.getError() instanceof IocException) {
            if (NutShiro.isAjax(ac.getRequest())) {
                NutShiro.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error(Mvcs.getMessage(ac.getRequest(), "system.exception")));
            } else {
                new ForwardView(errorUri).render(ac.getRequest(), ac.getResponse(), Mvcs.getMessage(ac.getRequest(), "system.exception"));
            }
        }
        super.process(ac);
    }
}
