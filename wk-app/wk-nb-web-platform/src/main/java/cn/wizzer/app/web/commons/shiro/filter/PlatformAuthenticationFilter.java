package cn.wizzer.app.web.commons.shiro.filter;


import cn.wizzer.app.web.commons.shiro.token.CaptchaToken;
import cn.wizzer.app.web.commons.utils.RSAUtil;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPrivateKey;

/**
 * Created by wizzer on 2017/1/10.
 */
public class PlatformAuthenticationFilter extends FormAuthenticationFilter implements ActionFilter {
    private final static Log log= Logs.get();
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
        try {
            RSAPrivateKey platformPrivateKey = (RSAPrivateKey) request.getSession().getAttribute("platformPrivateKey");
            if (platformPrivateKey != null) {
                password = RSAUtil.decryptByPrivateKey(password, platformPrivateKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CaptchaToken(username, password, rememberMe, host, captcha);
    }

    public View match(ActionContext actionContext) {
        HttpServletRequest request = actionContext.getRequest();
        AuthenticationToken authenticationToken = createToken(request);
        request.setAttribute("loginToken", authenticationToken);
        return null;
    }
}