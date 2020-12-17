package com.budwk.app.web.commons.shiro.filter;


import com.budwk.app.web.commons.shiro.token.PlatformCaptchaToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wizzer on 2017/1/10.
 */
@IocBean(name = "platformAuthc")
public class PlatformAuthenticationFilter extends FormAuthenticationFilter implements ActionFilter {
    private final static Log log = Logs.get();
    private String captchaParam = "platformCaptcha";
    private String captchaKey = "platformKey";

    public String getCaptchaParam() {
        return captchaParam;
    }

    public String getKey() {
        return captchaKey;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    protected String getKey(ServletRequest request) {
        return WebUtils.getCleanParam(request, getKey());
    }

    protected AuthenticationToken createToken(HttpServletRequest request) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
        String key = getKey(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return new PlatformCaptchaToken(username, password, rememberMe, host, captcha, key);
    }

    public View match(ActionContext actionContext) {
        HttpServletRequest request = actionContext.getRequest();
        AuthenticationToken authenticationToken = createToken(request);
        request.setAttribute("platformLoginToken", authenticationToken);
        return null;
    }
}