package com.xyz.beanmappermiddleware.adapter.out.api;


import com.xyz.beanmappermiddleware.application.port.in.dto.responses.speechtotext.SpeechToTextResponse;
import com.xyz.beanmappermiddleware.application.service.exception.SpeechToTextException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.config.constants.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.http.HttpConnectTimeoutException;


@Component
@Slf4j
public class STTFetchClient {
    private final WebClient webClient;


    @Value("${speechtotext.apiurl}")
    private String speechToTextBaseUrl;



    public STTFetchClient(@Qualifier("webClientWithTimeout")WebClient webClient ) {
        this.webClient = webClient;
    }



    public Mono<SpeechToTextResponse> fetchSpeechToTextResponse(FilePart resource, String format, String token) throws SpeechToTextException {
        final MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.asyncPart("files", resource.content(),DataBuffer.class)
                .filename(resource.filename());
        builder.part("format", format);
        return webClient
                .post()
                .uri(speechToTextBaseUrl)
                .headers(httpHeaders -> httpHeaders.add("Authorization", "Bearer " + token))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve().bodyToMono(SpeechToTextResponse.class)
                .doOnNext(speechToTextResponse -> log.info("Speech to text Response: {}", speechToTextResponse))
                .doOnError(error -> log.error("ERROR: Failed to get response from Speech to text api : {}", error.getMessage()))
                .onErrorMap(WebClientResponseException.class, error -> new SpeechToTextException(500, error.getStatusText(), error.getMessage()))
                .onErrorMap(HttpConnectTimeoutException.class, error -> new SpeechToTextException(500, Constants.FIELD_ERROR_REQUEST_TIMEOUT, error.getMessage()))
                .onErrorMap(ReadTimeoutException.class, error -> new SpeechToTextException(500, Constants.FIELD_ERROR_REQUEST_TIMEOUT, error.getMessage()))
                .onErrorMap(WriteTimeoutException.class, error -> new SpeechToTextException(500, Constants.FIELD_ERROR_REQUEST_TIMEOUT, error.getMessage()))
                .onErrorMap(WebClientRequestException.class, error -> new SpeechToTextException(500, Constants.FIELD_ERROR_INTERNAL_SERVER_ERROR, error.getMessage()))
                ;

    }

}
