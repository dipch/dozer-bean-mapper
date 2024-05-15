package com.xyz.beanmappermiddleware.core.keycloak.application.port.out;

import com.xyz.beanmappermiddleware.core.keycloak.domain.AuthorizationDetail;
import reactor.core.publisher.Mono;

public interface AuthorizationDetailPersistence {
    Mono<AuthorizationDetail> save(AuthorizationDetail authorizationDetail);
    Mono<AuthorizationDetail> retrieve();
}
