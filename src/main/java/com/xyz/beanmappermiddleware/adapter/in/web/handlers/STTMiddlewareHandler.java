package com.xyz.beanmappermiddleware.adapter.in.web.handlers;

import com.xyz.beanmappermiddleware.application.port.in.usecase.STTMiddlewareUseCase;
import com.xyz.beanmappermiddleware.application.service.exception.SpeechToTextException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class STTMiddlewareHandler {

    private final STTMiddlewareUseCase STTMiddlewareUseCase;

    public STTMiddlewareHandler(STTMiddlewareUseCase STTMiddlewareUseCase) {
        this.STTMiddlewareUseCase = STTMiddlewareUseCase;
    }

    public Mono<ServerResponse> getSpeechToTextMiddleWareResponse(ServerRequest serverRequest) {
        return serverRequest.multipartData()
                .mapNotNull(map -> map.getFirst("files"))
                .cast(FilePart.class)
                .flatMap(filePart -> {
                            String format = serverRequest.queryParam("format").orElse("m4a");
                            return STTMiddlewareUseCase.getSpeechToTextMiddlewareResponse(filePart, format)
                                    .flatMap(speechToTextResponse -> {
                                                log.info("Speech To Text Response: {}", speechToTextResponse);
                                                return ServerResponse
                                                        .ok()
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .bodyValue(speechToTextResponse);
                                            }
                                    );

                        }
                )
                .onErrorResume(SpeechToTextException.class, e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON).build()
                );

    }

}