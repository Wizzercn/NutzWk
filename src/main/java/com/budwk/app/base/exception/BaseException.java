package com.budwk.app.base.exception;

/**
 * @author wizzer@qq.com
 */
public class BaseException extends RuntimeException{

    private static final long serialVersionUID = -3148500348308095226L;

    public BaseException(String message) {
        super(message);
    }
}