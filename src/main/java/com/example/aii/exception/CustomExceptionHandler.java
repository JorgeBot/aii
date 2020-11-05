package com.example.aii.exception;

import org.apache.shiro.authc.IncorrectCredentialsException;
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
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handlerAuthenticationException(AuthenticationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ValidationFailedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handlerValidationFailedException(ValidationFailedException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handlerUnauthorizedException(UnauthorizedException e) {
        return "没有权限";
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handlerAuthorizationException(AuthorizationException e) {
        return "未授权访问";
    }

    @ExceptionHandler(AiiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handlerAiiException(AiiException e) {
        return e.getMessage();
    }
}
