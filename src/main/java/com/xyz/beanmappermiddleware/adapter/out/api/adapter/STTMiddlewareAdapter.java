package com.xyz.beanmappermiddleware.adapter.out.api.adapter;

import com.xyz.beanmappermiddleware.application.port.in.dto.responses.speechtotext.SpeechToTextResponse;
import com.xyz.beanmappermiddleware.application.port.out.STTMiddlewarePort;
import com.xyz.beanmappermiddleware.application.service.exception.SpeechToTextException;
import com.xyz.beanmappermiddleware.core.utils.ExceptionHandlerUtil;
import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.adapter.out.api.STTFetchClient;
import com.xyz.beanmappermiddleware.core.keycloak.application.port.in.AuthorizationDetailUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Slf4j
public class STTMiddlewareAdapter implements STTMiddlewarePort {

    private final STTFetchClient STTFetchClient;
    private final AuthorizationDetailUseCase authorizationDetailUseCase;


    public STTMiddlewareAdapter(STTFetchClient STTFetchClient, AuthorizationDetailUseCase authorizationDetailUseCase) {
        this.STTFetchClient = STTFetchClient;
        this.authorizationDetailUseCase = authorizationDetailUseCase;
    }


    @Override
    public Mono<SpeechToTextResponse> getSpeechToTextResponse(FilePart part, String format) throws SpeechToTextException {
        return authorizationDetailUseCase.getAccessToken()
                .flatMap(token -> getResponse(part, format,token))
                .onErrorResume(ExceptionHandlerUtil.class, e -> {
                    if (e.getCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                        return handleUnauthorizedError(part, format);
                    }
                    return Mono.error(e);
                });

    }

    public Mono<SpeechToTextResponse> getResponse(FilePart part, String format, String token) throws SpeechToTextException {
        return  STTFetchClient.fetchSpeechToTextResponse(part, format, token);
    }


    private Mono<SpeechToTextResponse> handleUnauthorizedError(FilePart part, String format) {
        return authorizationDetailUseCase.getAccessToken()
                .flatMap(token -> getResponse(part, format, token))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .jitter(0.5)
                        .filter(e -> e instanceof ExceptionHandlerUtil && ((ExceptionHandlerUtil) e).getCode().value() == HttpStatus.UNAUTHORIZED.value()));
    }
}
