package com.xyz.beanmappermiddleware.core.keycloak.adapter.out.web;

import com.xyz.beanmappermiddleware.core.keycloak.adapter.out.web.api.AuthorizationDetailKeyCloakAPI;
import com.xyz.beanmappermiddleware.core.keycloak.application.port.out.AuthorizationDetailClient;
import lombok.extern.slf4j.Slf4j;

import com.xyz.beanmappermiddleware.core.keycloak.domain.AuthorizationDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AuthorizationDetailKeyCloakClient implements AuthorizationDetailClient {

    private final AuthorizationDetailKeyCloakAPI authorizationDetailKeyCloakAPI;

    @Value("${xxxxx}")
    private String clientId;

    @Value("${xxxxx}")
    private String clientSecret;

    @Value("${xxxxxx}")
    private String grandType;

    public AuthorizationDetailKeyCloakClient(AuthorizationDetailKeyCloakAPI authorizationDetailKeyCloakAPI) {
        this.authorizationDetailKeyCloakAPI = authorizationDetailKeyCloakAPI;
    }

    @Override
    public Mono<AuthorizationDetail> getAuthorizationDetailFromClientAPI() {
        log.info("Requesting Keycloak to authenticate....");
        return authorizationDetailKeyCloakAPI.authenticateWithClientIdAndClientSecret(clientId, clientSecret, grandType)
                .map(response -> AuthorizationDetail
                        .builder()
                        .accessToken(response.getAccess_token())
                        .expiresIn(response.getExpires_in())
                        .notBeforePolicy(response.getNot_before_policy())
                        .refreshExpiresIn(response.getRefresh_expires_in())
                        .scope(response.getScope())
                        .createdOn(LocalDateTime.now())
                        .build());
    }

}

