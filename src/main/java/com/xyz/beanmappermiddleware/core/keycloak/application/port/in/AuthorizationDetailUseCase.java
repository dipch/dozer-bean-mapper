package com.xyz.beanmappermiddleware.core.keycloak.application.port.in;

import reactor.core.publisher.Mono;

public interface AuthorizationDetailUseCase {
    Mono<String> getAccessToken();
}
