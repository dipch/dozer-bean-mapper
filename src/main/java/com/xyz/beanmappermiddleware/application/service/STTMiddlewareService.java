package com.xyz.beanmappermiddleware.application.service;

import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.application.port.in.usecase.STTMiddlewareUseCase;
import com.xyz.beanmappermiddleware.application.port.in.dto.responses.speechtotext.SpeechToTextResponse;
import com.xyz.beanmappermiddleware.application.port.out.STTMiddlewarePort;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Service
@Slf4j
public class STTMiddlewareService implements STTMiddlewareUseCase {
    private final STTMiddlewarePort STTMiddlewarePort;


    public STTMiddlewareService(STTMiddlewarePort STTMiddlewarePort) {
        this.STTMiddlewarePort = STTMiddlewarePort;
    }


    @Override
    public Mono<SpeechToTextResponse> getSpeechToTextMiddlewareResponse(FilePart  multipartFile, String format) {
        return STTMiddlewarePort.getSpeechToTextResponse(multipartFile, format).flatMap(speechToTextResponse -> {
            speechToTextResponse.setTimeStamp(String.valueOf(LocalDateTime.now()));
            return Mono.just(speechToTextResponse);
        });
    }
}