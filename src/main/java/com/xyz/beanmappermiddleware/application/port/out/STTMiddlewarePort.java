package com.xyz.beanmappermiddleware.application.port.out;

import com.xyz.beanmappermiddleware.application.port.in.dto.responses.speechtotext.SpeechToTextResponse;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface STTMiddlewarePort {
    Mono<SpeechToTextResponse> getSpeechToTextResponse(FilePart multipartFile, String format);
}
