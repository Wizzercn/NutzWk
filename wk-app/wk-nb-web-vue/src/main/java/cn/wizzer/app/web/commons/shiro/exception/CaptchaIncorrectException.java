package cn.wizzer.app.web.commons.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by wizzer on 2017/1/10.
 */
public class CaptchaIncorrectException extends AuthenticationException {

    public CaptchaIncorrectException() {
        super();
    }

    public CaptchaIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaIncorrectException(String message) {
        super(message);
    }

    public CaptchaIncorrectException(Throwable cause) {
        super(cause);
    }
}
