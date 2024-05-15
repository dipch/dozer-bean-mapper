package com.xyz.beanmappermiddleware.core.utils;

import com.xyz.beanmappermiddleware.application.service.exception.InvalidChatbotResponseException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@UtilityClass
public class ErrorHandler {
    public Mono<ServerResponse> buildErrorResponseForBusiness(InvalidChatbotResponseException ex, ServerRequest request) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .header("type", "apiError")
                .bodyValue(new ErrorBody(ex, request.path()));
    }
    public Mono<ServerResponse> buildErrorResponseForUncaught(Throwable ex, ServerRequest request) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .header("type", "apiError")
                .bodyValue(new ErrorBody(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        "Something bad happened. Please try again later.", request.path()));
    }
    public Mono<ServerResponse> buildErrorResponseRequestValidation(String ex, ServerRequest request) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(new ErrorBody(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), ex, request.path()));
    }
}