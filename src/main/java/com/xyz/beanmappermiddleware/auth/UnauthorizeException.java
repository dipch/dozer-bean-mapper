package com.xyz.beanmappermiddleware.auth;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizeException extends ResponseStatusException {

    @Nullable
    private final MethodParameter parameter;


    public UnauthorizeException(String reason) {
        this(reason, null, null);
    }


    public UnauthorizeException(String reason, @Nullable MethodParameter parameter) {
        this(reason, parameter, null);
    }


    public UnauthorizeException(String reason, @Nullable MethodParameter parameter, @Nullable Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, reason, cause);
        this.parameter = parameter;
    }


    @Nullable
    public MethodParameter getMethodParameter() {
        return this.parameter;
    }

}