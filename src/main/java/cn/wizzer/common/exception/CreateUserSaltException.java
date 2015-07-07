package cn.wizzer.common.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Wizzer.cn on 2015/7/1.
 */
public class CreateUserSaltException extends AuthenticationException {

    private static final long serialVersionUID = 3315875923669742156L;

    public CreateUserSaltException() {
        super();
    }

    public CreateUserSaltException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateUserSaltException(String message) {
        super(message);
    }

    public CreateUserSaltException(Throwable cause) {
        super(cause);
    }
}
