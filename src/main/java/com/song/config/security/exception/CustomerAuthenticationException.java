package com.song.config.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义异常类
 */
public class CustomerAuthenticationException extends AuthenticationException {

    public CustomerAuthenticationException(String message){
        super(message);
    }
}
