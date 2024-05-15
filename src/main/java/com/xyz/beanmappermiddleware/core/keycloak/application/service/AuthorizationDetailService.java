package com.xyz.beanmappermiddleware.core.keycloak.application.service;

import com.xyz.beanmappermiddleware.core.keycloak.application.port.out.AuthorizationDetailClient;
import com.xyz.beanmappermiddleware.core.keycloak.application.port.out.AuthorizationDetailPersistence;
import com.xyz.beanmappermiddleware.core.utils.ExceptionHandlerUtil;
import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.core.keycloak.application.port.in.AuthorizationDetailUseCase;
import com.xyz.beanmappermiddleware.core.keycloak.domain.AuthorizationDetail;
import com.xyz.beanmappermiddleware.core.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthorizationDetailService implements AuthorizationDetailUseCase {

    private final AuthorizationDetailPersistence authorizationDetailPersistence;
    private final AuthorizationDetailClient authorizationDetailClient;

    public AuthorizationDetailService(AuthorizationDetailPersistence authorizationDetailPersistence, AuthorizationDetailClient authorizationDetailClient) {
        this.authorizationDetailPersistence = authorizationDetailPersistence;
        this.authorizationDetailClient = authorizationDetailClient;
    }

    @Override
    public Mono<String> getAccessToken() {
        return authorizationDetailPersistence.retrieve()
                .switchIfEmpty(authenticateAndSave())
                .flatMap(this::onAccessTokenExpired)
                .map(AuthorizationDetail::getAccessToken)
                .onErrorResume(throwable -> Mono.error(new ExceptionHandlerUtil(HttpStatus.UNAUTHORIZED, Messages.UNAUTHORIZED_EXCEPTION_MESSAGE)));
    }


    private Mono<AuthorizationDetail> authenticateAndSave() {
        return authorizationDetailClient.getAuthorizationDetailFromClientAPI()
                .doOnRequest(value -> log.info("Authorization detail not found in Redis, getting token from keycloak...."))
                .doOnSuccess(authorizationDetail -> log.info("Successfully get AuthorizationDetail from keycloak API : {}", authorizationDetail))
                .doOnError(throwable -> log.error("Exception occured during getting data from keycloak API : {}", throwable.getMessage()))
                .flatMap(this::save)
                .onErrorResume(throwable -> Mono.error(new ExceptionHandlerUtil(HttpStatus.UNAUTHORIZED, Messages.UNAUTHORIZED_EXCEPTION_MESSAGE)));
    }

    private Mono<AuthorizationDetail> save(AuthorizationDetail authorizationDetail) {
        return authorizationDetailPersistence.save(authorizationDetail)
                .doOnRequest(value -> log.info("Saving Data in Redis...."))
                .doOnSuccess(savedAuthorizationDetail -> log.info("Successfully saved authorization detail in redis : {}", savedAuthorizationDetail))
                .doOnError(throwable -> log.error("Exception occurred during saving authorization detail in redis : {}", throwable.getMessage()))
                .onErrorResume(throwable -> Mono.error(new ExceptionHandlerUtil(HttpStatus.UNAUTHORIZED, Messages.AUTHORIZATION_DETAIL_SAVE_EXCEPTION_MSG)));
    }

    private Mono<AuthorizationDetail> onAccessTokenExpired(AuthorizationDetail authorizationDetail) {
        if (authorizationDetail.getCreatedOn().plusSeconds(authorizationDetail.getExpiresIn()).isAfter(LocalDateTime.now()))
            return Mono.just(authorizationDetail);
        else {
            log.info("Token Expired, getting new token from keycloak API.....");
            return authenticateAndSave();
        }
    }

}
