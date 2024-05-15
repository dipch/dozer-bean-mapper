package com.xyz.beanmappermiddleware.auth;

import io.netty.handler.timeout.ReadTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.http.HttpConnectTimeoutException;

@Slf4j
@Component
public class TokenAuthorizationClient {

    private final WebClient webClient;
    @Value("${verify.token.url}")
    private String AUTHENTICATION_URL_TEMPLATE;

    public TokenAuthorizationClient(@Qualifier("webClientWithTimeout")WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<TokenAuthorizationResponse> authenticateToken(String token) throws AuthException{
        return webClient
                .get()
                .uri(AUTHENTICATION_URL_TEMPLATE)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .bodyToMono(TokenAuthorizationResponse.class)
                .doOnError(throwable -> log.error("Failed to get auth info from authServer : {}", throwable.getMessage()))
                .doOnSuccess(authInfo -> log.info("Successfully get response  from auth : {}", authInfo))
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("Webclient exception happened: {}, {}",e.getStatusCode(),e.getMessage());
                    return new AuthException(e.getStatusCode(), e.getLocalizedMessage());
                })
                .doOnRequest(value -> log.info("request accept for getting auth info for token: {}",token))
                .onErrorMap(HttpConnectTimeoutException.class, e -> new AuthException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()))
                .onErrorMap(ReadTimeoutException.class, e -> new AuthException(HttpStatus.INTERNAL_SERVER_ERROR,e.getLocalizedMessage()));
    }
}
