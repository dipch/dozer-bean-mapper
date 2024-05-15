package com.xyz.beanmappermiddleware.application.port.in.usecase;

import com.xyz.beanmappermiddleware.application.port.in.dto.responses.speechtotext.SpeechToTextResponse;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface STTMiddlewareUseCase {
    Mono<SpeechToTextResponse> getSpeechToTextMiddlewareResponse(FilePart multipartFile, String format);

}
