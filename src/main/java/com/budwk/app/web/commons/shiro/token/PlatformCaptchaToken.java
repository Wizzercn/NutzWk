package com.budwk.app.web.commons.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created by wizzer on 2017/1/11.
 */
public class PlatformCaptchaToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 4676958151524148623L;
    private String captcha;
    private String key;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PlatformCaptchaToken(String username, String password, boolean rememberMe, String host, String captcha, String key) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.key = key;
    }
}
