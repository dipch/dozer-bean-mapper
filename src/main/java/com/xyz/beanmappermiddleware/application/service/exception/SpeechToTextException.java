package com.xyz.beanmappermiddleware.application.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class SpeechToTextException extends RuntimeException{
    private Integer code;
    private String error;
    private String errorDescription;

}
