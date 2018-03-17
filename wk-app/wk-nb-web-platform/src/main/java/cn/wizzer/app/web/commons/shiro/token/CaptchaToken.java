package cn.wizzer.app.web.commons.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created by wizzer on 2017/1/11.
 */
public class CaptchaToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 4676958151524148623L;
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public CaptchaToken(String username, String password, boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }
}
