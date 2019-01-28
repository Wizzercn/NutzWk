package cn.wizzer.app.web.commons.shiro.filter;


import cn.wizzer.app.web.commons.shiro.token.PlatformCaptchaToken;
import cn.wizzer.app.web.commons.utils.RSAUtil;
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
import java.security.interfaces.RSAPrivateKey;

/**
 * Created by wizzer on 2017/1/10.
 */
@IocBean(name = "platformAuthc")
public class PlatformAuthenticationFilter extends FormAuthenticationFilter implements ActionFilter {
    private final static Log log = Logs.get();
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
//        try {
//            RSAPrivateKey platformPrivateKey = (RSAPrivateKey) request.getSession().getAttribute("platformPrivateKey");
//            if (platformPrivateKey != null) {
//                password = RSAUtil.decryptByPrivateKey(password, platformPrivateKey);
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            /*
//            若程序抛异常：java.lang.SecurityException: JCE cannot authenticate the provider BC
//            1、编辑文件 /usr/java/jdk1.8.0_162/jre/lib/security/java.security
//            在9下面添加 security.provider.10=org.bouncycastle.jce.provider.BouncyCastleProvider
//            2、拷贝 bcprov-jdk16-143.jar 和 bcprov-jdk15-135.jar 到 /usr/java/jdk1.8.0_162/jre/lib/ext 目录下
//             */
//            log.error(e.getMessage(), e);
//        }
        return new PlatformCaptchaToken(username, password, rememberMe, host, captcha);
    }

    public View match(ActionContext actionContext) {
        HttpServletRequest request = actionContext.getRequest();
        AuthenticationToken authenticationToken = createToken(request);
        request.setAttribute("platformLoginToken", authenticationToken);
        return null;
    }
}