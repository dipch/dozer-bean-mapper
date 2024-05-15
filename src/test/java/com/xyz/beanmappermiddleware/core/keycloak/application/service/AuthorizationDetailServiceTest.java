package com.xyz.beanmappermiddleware.core.keycloak.application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuthorizationDetailServiceTest {
    @Autowired
    private AuthorizationDetailService service;

    @Test
    void contextLoad() {
        assertNotNull(service);
    }

    @Test
    void giveKeyCloakAccessTokeCredentials_whenGetAccessTokenIsExecuted_thenItWillReturnAccessToken() {
        //when
        Mono<String> accessToken = service.getAccessToken();
        //then
        StepVerifier
                .create(accessToken)
                .expectSubscription()
//                .assertNext(s -> accessToken.log())
//                .assertNext(s -> System.out.println("Access Token >> " + accessToken))
                .assertNext (Assertions::assertNotNull)
                .expectComplete()
                .verify()
        ;
    }
}