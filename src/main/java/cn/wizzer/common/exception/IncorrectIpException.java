package cn.wizzer.common.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Wizzer.cn on 2015/7/1.
 */
public class IncorrectIpException extends AuthenticationException {

    public IncorrectIpException() {
        super();
    }

    public IncorrectIpException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectIpException(String message) {
        super(message);
    }

    public IncorrectIpException(Throwable cause) {
        super(cause);
    }
}
