package com.xyz.beanmappermiddleware.application.service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ChatbotAPIErrorResponseDTO {
    private String recipientId;
    private Integer code;
    private String error;
    private String errorDescription;
}
