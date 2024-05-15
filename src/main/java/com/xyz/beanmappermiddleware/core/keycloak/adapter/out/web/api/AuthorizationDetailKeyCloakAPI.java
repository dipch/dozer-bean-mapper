package com.xyz.beanmappermiddleware.core.keycloak.adapter.out.web.api;

import com.xyz.beanmappermiddleware.core.keycloak.adapter.out.web.dto.AuthorizationDetailKeyCloakResponse;
import com.xyz.beanmappermiddleware.core.utils.ExceptionHandlerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationDetailKeyCloakAPI {

    private final WebClient webClient;
    private static final String KEY_CLOAK_URL_TEMPLATE = "/auth/realms/vision-service/protocol/openid-connect/token";


    public AuthorizationDetailKeyCloakAPI(@Qualifier("keyCloakWebClient") WebClient webClient) {
                this.webClient = webClient;
    }

    public Mono<AuthorizationDetailKeyCloakResponse> authenticateWithClientIdAndClientSecret(String clientId, String clientSecret, String grandType) {
        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
        bodyValues.add("client_id", clientId);
        bodyValues.add("client_secret", clientSecret);
        bodyValues.add("grant_type", grandType);
        return webClient
                .post()
                .uri(KEY_CLOAK_URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(bodyValues))
                .retrieve()
                .bodyToMono(AuthorizationDetailKeyCloakResponse.class)
                .doOnError(throwable ->  log.error("Failed to get access token from keycloak : {}", throwable.getMessage()))
                .doOnSuccess(authorizationDetailKeyCloakResponse -> log.info("Successfully get response from keycloak : {}", authorizationDetailKeyCloakResponse))
                .onErrorMap(throwable -> new ExceptionHandlerUtil(HttpStatus.UNAUTHORIZED, throwable.getMessage()));
    }

}

