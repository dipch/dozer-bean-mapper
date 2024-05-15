package com.xyz.beanmappermiddleware.core.utils;

import com.xyz.beanmappermiddleware.application.service.exception.InvalidChatbotResponseException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
//@Builder
public class ErrorBody implements Serializable {
    private final long timestamp = System.currentTimeMillis();
    private String recipientId;
    private Integer code;
    private String error;
    private String errorDescription;
    private String path;
    public ErrorBody(InvalidChatbotResponseException error, String path) {
        this.recipientId = null;
        this.error = error.getError();
        this.errorDescription = error.getErrorDescription();
        this.code = error.getCode();
        this.path = path;
    }
    public ErrorBody(int code, String error, String errorDescription, String path) {
        this.recipientId = null;
        this.error = error;
        this.errorDescription = errorDescription;
        this.code = code;
        this.path = path;
    }
}