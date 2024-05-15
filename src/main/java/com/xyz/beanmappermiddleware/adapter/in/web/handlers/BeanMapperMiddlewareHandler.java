package com.xyz.beanmappermiddleware.adapter.in.web.handlers;

import com.xyz.beanmappermiddleware.application.port.in.dto.request.MiddlewareRequestDTO;
import com.xyz.beanmappermiddleware.application.port.in.dto.responses.middlewareresponse.MiddlewareResponseDTO;
import com.xyz.beanmappermiddleware.application.port.in.usecase.BeanMapperMiddlewareUseCase;
import com.xyz.beanmappermiddleware.application.service.exception.InvalidChatbotResponseException;
import com.xyz.beanmappermiddleware.core.validator.AbstractValidationHandler;
import com.xyz.beanmappermiddleware.core.validator.MiddlewareRequestDTOValidator;
import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.config.helpers.TypeMappings;
import com.xyz.beanmappermiddleware.core.utils.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Component
@Slf4j
public class BeanMapperMiddlewareHandler extends AbstractValidationHandler<MiddlewareRequestDTO, MiddlewareRequestDTOValidator> {

    private final BeanMapperMiddlewareUseCase beanMapperMiddlewareUseCase;

    private String headerType;

    public BeanMapperMiddlewareHandler(BeanMapperMiddlewareUseCase beanMapperMiddlewareUseCase) {
        super(MiddlewareRequestDTO.class, new MiddlewareRequestDTOValidator());
        this.beanMapperMiddlewareUseCase = beanMapperMiddlewareUseCase;
    }

    @Override
    protected Mono<ServerResponse> processBody(MiddlewareRequestDTO validBody, ServerRequest originalRequest) {
        return originalRequest.principal().flatMap(principal -> {
                    log.info("request get from userId : {}, sessionId : {}, request : {}", principal.getName(), validBody.getSender(), validBody);
                    return beanMapperMiddlewareUseCase.getMiddlewareResponse(validBody, principal.getName());
                })
                .flatMap(this::generateResponseHeaderType)
                .flatMap(middlewareResponseDTO -> {
                            log.info("Middleware Response: {}", middlewareResponseDTO);
                            if (beanMapperMiddlewareUseCase.checkIfChatbotResponseBodyContainsErrorCode401(middlewareResponseDTO)) {
                                return ServerResponse
                                        .status(HttpStatus.UNAUTHORIZED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .header("type", this.headerType)
                                        .bodyValue(middlewareResponseDTO);
                            }
                            if (beanMapperMiddlewareUseCase.checkIfChatbotResponseBodyContainsErrorCode423(middlewareResponseDTO)) {
                                return ServerResponse
                                        .status(HttpStatus.LOCKED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .header("type", this.headerType)
                                        .bodyValue(middlewareResponseDTO);
                            }
                            return ServerResponse
                                    .ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("type", this.headerType)
                                    .bodyValue(middlewareResponseDTO);
                        }
                )
                .onErrorResume(InvalidChatbotResponseException.class, e -> ErrorHandler.buildErrorResponseForBusiness(e, originalRequest))
                .onErrorResume(Predicate.not(InvalidChatbotResponseException.class::isInstance), e -> ErrorHandler.buildErrorResponseForUncaught(e, originalRequest));
    }

    public Mono<MiddlewareResponseDTO> generateResponseHeaderType(MiddlewareResponseDTO middlewareResponseDTO) {
        this.headerType = TypeMappings.getMiddlewareResponseHeaderType(middlewareResponseDTO.getType());
        beanMapperMiddlewareUseCase.setMiddlewareResponseDTOTitleAndNullProperties(middlewareResponseDTO, headerType);
        return Mono.just(middlewareResponseDTO);
    }

}