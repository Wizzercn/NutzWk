package cn.wizzer.app.web.commons.shiro.filter;


import cn.wizzer.framework.shiro.token.CaptchaToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wizzer on 2017/1/10.
 */
public class PlatformAuthenticationFilter extends FormAuthenticationFilter implements ActionFilter {

    private String captchaParam = "platformCaptcha";

    public String getCaptchaParam() {
        return captchaParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    protected AuthenticationToken createToken(HttpServletRequest request) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return new CaptchaToken(username, password, rememberMe, host, captcha);
    }

    public View match(ActionContext actionContext) {
        HttpServletRequest request = actionContext.getRequest();
        AuthenticationToken authenticationToken = createToken(request);
        request.setAttribute("loginToken", authenticationToken);
        return null;
    }
}