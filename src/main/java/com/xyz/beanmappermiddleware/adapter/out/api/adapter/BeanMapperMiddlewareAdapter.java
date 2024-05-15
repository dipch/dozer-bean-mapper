package com.xyz.beanmappermiddleware.adapter.out.api.adapter;

import com.xyz.beanmappermiddleware.application.port.in.dto.request.ChatbotRequestDTO;
import com.xyz.beanmappermiddleware.application.port.in.dto.request.MiddlewareRequestDTO;
import com.xyz.beanmappermiddleware.application.port.in.dto.responses.chatbotresponse.ChatbotResponseDTO;
import com.xyz.beanmappermiddleware.application.port.out.BeanMapperMiddlewarePort;
import com.xyz.beanmappermiddleware.application.service.exception.InvalidChatbotResponseException;
import com.xyz.beanmappermiddleware.core.utils.ExceptionHandlerUtil;
import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.adapter.out.api.ChatbotResponseFetchClient;
import com.xyz.beanmappermiddleware.core.keycloak.application.port.in.AuthorizationDetailUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;


@Component
@Slf4j
public class BeanMapperMiddlewareAdapter implements BeanMapperMiddlewarePort {

    private final ChatbotResponseFetchClient chatbotResponseFetchClient;
    private final AuthorizationDetailUseCase authorizationDetailUseCase;

    public BeanMapperMiddlewareAdapter(ChatbotResponseFetchClient chatbotResponseFetchClient, AuthorizationDetailUseCase authorizationDetailUseCase) {
        this.chatbotResponseFetchClient = chatbotResponseFetchClient;
        this.authorizationDetailUseCase = authorizationDetailUseCase;
    }

    @Override
    public Mono<List<ChatbotResponseDTO>> getChatbotResponse(MiddlewareRequestDTO requestDTO) throws InvalidChatbotResponseException {
       return authorizationDetailUseCase.getAccessToken()
               .flatMap(token -> getResponse(requestDTO, token))
               .onErrorResume(ExceptionHandlerUtil.class, e -> {
                   if (e.getCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                       return handleUnauthorizedError(requestDTO);
                   }
                   return Mono.error(e);
               });
    }

    private Mono<List<ChatbotResponseDTO>> getResponse(MiddlewareRequestDTO requestDTO, String token) {
        return chatbotResponseFetchClient.fetchChatbotResponse(buildRequestBody(requestDTO, requestDTO.getMessage()), token)
                .collectList()
                .flatMap(listResponse -> {
                            if (listResponse.isEmpty()) {
                                log.info("response is empty. terminating calling");
                                return chatbotResponseFetchClient.fetchChatbotResponse(buildRequestBody(requestDTO, "/terminate_flow"), token)
                                        .collectList();
                            } else {
                                log.info("response is not empty. All good");
                                return Mono.just(listResponse);
                            }
                        }
                );
    }


    private ChatbotRequestDTO buildRequestBody(MiddlewareRequestDTO requestDTO, String message) {
        log.info("request dto : {}", requestDTO);
        return ChatbotRequestDTO.builder()
                .sender(requestDTO.getSender())
                .access_token(requestDTO.getAccessToken())
                .message(message)
                .build();
    }


    private  Mono<List<ChatbotResponseDTO>> handleUnauthorizedError(MiddlewareRequestDTO requestDTO) {
        return authorizationDetailUseCase.getAccessToken()
                .flatMap(token -> getResponse(requestDTO, token))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .jitter(0.5)
                        .filter(e -> e instanceof ExceptionHandlerUtil && ((ExceptionHandlerUtil) e).getCode().value() == HttpStatus.UNAUTHORIZED.value()));
    }
}
