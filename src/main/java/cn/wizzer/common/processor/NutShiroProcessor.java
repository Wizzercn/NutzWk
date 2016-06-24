package cn.wizzer.common.processor;

import cn.wizzer.common.base.Result;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.nutz.integration.shiro.NutShiro;
import org.nutz.integration.shiro.NutShiroInterceptor;
import org.nutz.integration.shiro.NutShiroMethodInterceptor;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.View;
import org.nutz.mvc.impl.processor.AbstractProcessor;
import org.nutz.mvc.view.ServerRedirectView;

/**
 * Created by wizzer on 2016/6/24.
 */
public class NutShiroProcessor extends AbstractProcessor {

    protected NutShiroMethodInterceptor interceptor;

    protected String loginUri = "/private/login";

    protected String noAuthUri = "/private/login";

    protected boolean match;

    protected boolean init;

    public NutShiroProcessor() {
        interceptor = new NutShiroMethodInterceptor();
    }

    public void init(NutConfig config, ActionInfo ai) throws Throwable {
        if (init) // 禁止重复初始化,常见于ioc注入且使用了单例
            throw new IllegalStateException("this Processor have bean inited!!");
        super.init(config, ai);
        match = NutShiro.match(ai.getMethod());
        init = true;
    }

    public void process(ActionContext ac) throws Throwable {
        if (match) {
            try {
                interceptor.assertAuthorized(new NutShiroInterceptor(ac));
            } catch (Exception e) {
                whenException(ac, e);
                return;
            }
        }
        doNext(ac);
    }

    protected void whenException(ActionContext ac, Exception e) throws Throwable {
        Object val = ac.getRequest().getAttribute("shiro_auth_error");
        if (val != null && val instanceof View) {
            ((View) val).render(ac.getRequest(), ac.getResponse(), null);
            return;
        }
        if (e instanceof UnauthenticatedException) {
            whenUnauthenticated(ac, (UnauthenticatedException) e);
        } else if (e instanceof UnauthorizedException) {
            whenUnauthorized(ac, (UnauthorizedException) e);
        } else {
            whenOtherException(ac, e);
        }
    }

    protected void whenUnauthenticated(ActionContext ac, UnauthenticatedException e) throws Exception {
        if (NutShiro.isAjax(ac.getRequest())) {
            ac.getResponse().addHeader("loginStatus", "accessDenied");
            NutShiro.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error("登录失效", ac.getRequest()));
        } else {
            new ServerRedirectView(loginUri).render(ac.getRequest(), ac.getResponse(), null);
        }
    }

    protected void whenUnauthorized(ActionContext ac, UnauthorizedException e) throws Exception {
        if (NutShiro.isAjax(ac.getRequest())) {
            ac.getResponse().addHeader("loginStatus", "unauthorized");
            NutShiro.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error("登录失效", ac.getRequest()));
        } else {
            new ServerRedirectView(noAuthUri).render(ac.getRequest(), ac.getResponse(), null);
        }
    }

    protected void whenOtherException(ActionContext ac, Exception e) throws Exception {
        if (NutShiro.isAjax(ac.getRequest())) {
            ac.getResponse().addHeader("loginStatus", "accessDenied");
            NutShiro.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error("登录失效", ac.getRequest()));
        } else {
            new ServerRedirectView(loginUri).render(ac.getRequest(), ac.getResponse(), null);
        }
    }
}