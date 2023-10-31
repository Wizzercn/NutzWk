package com.budwk.app.base.exception;

/**
 * @author wizzer@qq.com
 */
public class BaseException extends RuntimeException{

    private static final long serialVersionUID = 7192152812384031563L;

    public BaseException(String message) {
        super(message);
    }
}