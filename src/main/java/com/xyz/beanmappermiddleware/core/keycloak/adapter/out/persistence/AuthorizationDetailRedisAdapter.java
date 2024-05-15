package com.xyz.beanmappermiddleware.core.keycloak.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.xyz.beanmappermiddleware.core.keycloak.application.port.out.AuthorizationDetailPersistence;
import com.xyz.beanmappermiddleware.core.utils.ExceptionHandlerUtil;
import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.core.keycloak.domain.AuthorizationDetail;
import com.xyz.beanmappermiddleware.core.utils.Messages;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationDetailRedisAdapter implements AuthorizationDetailPersistence {

    private final ReactiveValueOperations<String, String> reactiveValueOps;
    private final ObjectMapper objectMapper;

    @Value("${xxxxxxxxxxxxxx}")
    private String key;

    public AuthorizationDetailRedisAdapter(@Qualifier("reactiveValueOperations") ReactiveValueOperations<String, String> reactiveValueOps, ObjectMapper objectMapper) {
        this.reactiveValueOps = reactiveValueOps;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<AuthorizationDetail> save(AuthorizationDetail authorizationDetail) {
        String authorizationDetailValue;
        try {
            authorizationDetailValue = objectMapper.writeValueAsString(authorizationDetail);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert authorization detail to string ! {}", e.getMessage());
            return Mono.error(new ExceptionHandlerUtil( HttpStatus.INTERNAL_SERVER_ERROR, Messages.AUTHORIZATION_DETAIL_STRING_MAPPING_EXCEPTION_MSG));
        }
        return reactiveValueOps.set(key, authorizationDetailValue)
                .doOnRequest(value -> log.info("Saving Authorization Detail in Redis....."))
                .flatMap(aBoolean -> aBoolean ? reactiveValueOps.get(key) : Mono.error(new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.AUTHORIZATION_DETAIL_SAVE_EXCEPTION_MSG)))
                .flatMap(this::getAuthorizationDetailFromString)
                .doOnSuccess(ad -> log.info("Successfully saved authorization detail {}", ad))
                .doOnError(throwable -> log.error("Failed to save authorization detail {}", throwable.getMessage()))
                .onErrorResume(throwable -> Mono.error(new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.AUTHORIZATION_DETAIL_SAVE_EXCEPTION_MSG)));
    }

    @Override
    public Mono<AuthorizationDetail> retrieve() {
        return reactiveValueOps.get(key)
                .doOnRequest(value -> log.info("Getting authorization detail from redis...."))
                .flatMap(this::getAuthorizationDetailFromString)
                .doOnSuccess(authorizationDetail -> log.info("Successfully get authorization detail from redis {}", authorizationDetail))
                .doOnError(throwable -> log.error("Failed to get authorization detail from redis ! {}", throwable.getMessage()))
                .onErrorResume(throwable -> Mono.error(new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.AUTHORIZATION_DETAIL_PERSISTENCE_EXCEPTION_MSG)));
    }

    private Mono<AuthorizationDetail> getAuthorizationDetailFromString(String authorizationDetailStringValue) {
        AuthorizationDetail authorizationDetail;
        try {
            authorizationDetail = objectMapper.readValue(authorizationDetailStringValue, AuthorizationDetail.class);
        } catch (JsonProcessingException e) {
            return Mono.error(new ExceptionHandlerUtil(HttpStatus.INTERNAL_SERVER_ERROR, Messages.AUTHORIZATION_DETAIL_MAP_EXCEPTION_MSG));
        }
        return Mono.just(authorizationDetail);
    }


}

