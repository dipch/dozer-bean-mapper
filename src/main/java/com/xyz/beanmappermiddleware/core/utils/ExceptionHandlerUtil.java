package com.xyz.beanmappermiddleware.core.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ExceptionHandlerUtil extends Exception {
    public HttpStatus code;
    public String message;
    private String reason;
    private String request;
    private String response;

    public ExceptionHandlerUtil(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public ExceptionHandlerUtil(HttpStatus code, String message, String reason) {
        this.code = code;
        this.message = message;
        this.reason = reason;
    }

    public ExceptionHandlerUtil(HttpStatus code, String message, String reason, String request) {
        this.code = code;
        this.message = message;
        this.reason = reason;
        this.request = request;
    }

    public ExceptionHandlerUtil(HttpStatus code, String message, String reason, String request, String response) {
        this.code = code;
        this.message = message;
        this.reason = reason;
        this.response = response;
        this.request = request;
    }
}
