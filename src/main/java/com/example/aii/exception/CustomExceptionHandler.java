package com.example.aii.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.apache.shiro.authc.AuthenticationException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String handlerAuthenticationException(AuthenticationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handlerUnauthorizedException(UnauthorizedException e) {
        System.out.println("没有权限");
        return "没有权限";
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handlerAuthorizationException(AuthorizationException e) {
        System.out.println("未授权访问");
        return "未授权访问";
    }
}