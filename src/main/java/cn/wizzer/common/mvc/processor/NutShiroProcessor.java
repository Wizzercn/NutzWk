package cn.wizzer.common.mvc.processor;

import cn.wizzer.common.Message;
import org.nutz.integration.shiro.NutShiro;
import org.nutz.integration.shiro.NutShiroInterceptor;
import org.nutz.integration.shiro.NutShiroMethodInterceptor;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.impl.processor.AbstractProcessor;
import org.nutz.mvc.view.ServerRedirectView;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
public class NutShiroProcessor extends AbstractProcessor {
    protected NutShiroMethodInterceptor interceptor = new NutShiroMethodInterceptor();
    protected String uri;
    protected boolean match;
    protected boolean init;

    public NutShiroProcessor() {
        this.uri = "/private/login";
    }

    public void init(NutConfig config, ActionInfo ai) throws Throwable {
        if(this.init) {
            throw new IllegalStateException("this Processor have bean inited!!");
        } else {
            super.init(config, ai);
            this.match = NutShiro.match(ai.getMethod());
            this.init = true;
        }
    }

    public void process(ActionContext ac) throws Throwable {
        if(this.match) {
            try {
                this.interceptor.assertAuthorized(new NutShiroInterceptor(ac));
            } catch (Throwable var3) {
                if(NutShiro.isAjax(ac.getRequest())) {
                    ac.getResponse().setHeader("sessionstatus","timeout");
                    NutShiro.rendAjaxResp(ac.getRequest(), ac.getResponse(), (Message.error(var3.getMessage(), ac.getRequest())));
                } else {
                    (new ServerRedirectView(this.uri)).render(ac.getRequest(), ac.getResponse(), (Object)null);
                }

                return;
            }
        }

        this.doNext(ac);
    }
}
