package com.xyz.beanmappermiddleware.adapter.out.api;

import com.xyz.beanmappermiddleware.application.port.in.dto.request.ChatbotRequestDTO;
import com.xyz.beanmappermiddleware.application.port.in.dto.responses.chatbotresponse.ChatbotResponseDTO;
import com.xyz.beanmappermiddleware.application.service.exception.InvalidChatbotResponseException;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.config.constants.Constants;
import com.xyz.beanmappermiddleware.core.utils.CommonFunctions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.xyz.beanmappermiddleware.config.constants.Constants.FIELD_ERROR_INTERNAL_SERVER_ERROR;

@Component
@Slf4j
public class ChatbotResponseFetchClient {
    private final WebClient webClient;
    @Value("${chatbot.apiurl}")
    private String chatbotBaseUrl;

    public ChatbotResponseFetchClient(@Qualifier("webClientWithTimeout") WebClient webClient) {
            this.webClient = webClient;
    }


    public Flux<ChatbotResponseDTO> fetchChatbotResponse(ChatbotRequestDTO requestDto, String token) throws InvalidChatbotResponseException {
     return webClient
                .post()
                .uri(chatbotBaseUrl)
                .headers(httpHeaders -> httpHeaders.addAll(CommonFunctions.getHeadersConsumer(token)))
                .body(Mono.just(requestDto), ChatbotRequestDTO.class)
                .retrieve()
                .bodyToFlux(ChatbotResponseDTO.class)
                .doOnNext(chatbotResponseDTO -> log.info("Chatbot Response: {}", chatbotResponseDTO))
                .doOnError(error -> log.error("ERROR: Failed to get response from Chatbot api : {}", error.getMessage()))
                .doOnComplete(() -> log.info("Completed fetching response from Chatbot api"))
                .onErrorResume(e -> {
                    if (e instanceof WebClientRequestException) {
                        if (e.getCause() instanceof ReadTimeoutException ex) {
                            log.error("ReadTimeoutException - occurred while api call, error: {}", ex.getMessage());
                            return Mono.error(new InvalidChatbotResponseException(HttpStatus.REQUEST_TIMEOUT.value(), Constants.FIELD_ERROR_REQUEST_TIMEOUT, ex.getMessage()));
                        }
                        log.error("WebClientRequestException occurred while api call: {}", e.getMessage());
                        return Mono.error(new InvalidChatbotResponseException(HttpStatus.FAILED_DEPENDENCY.value(), FIELD_ERROR_INTERNAL_SERVER_ERROR, e.getMessage()));
                    } else if (e instanceof WebClientResponseException ex) {
                        log.error("WebClientResponseException occurred while api call, message: {}, status: {}, body : {}", ex.getMessage(), ex.getStatusCode(), ex.getResponseBodyAsString());
                        return Mono.error(new InvalidChatbotResponseException(ex.getStatusCode().value(), ex.getMessage(), ex.getResponseBodyAsString()));
                    } else {
                        log.error("unknown error occurred while api call, error: {}", e.getMessage());
                        return Mono.error(new InvalidChatbotResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), FIELD_ERROR_INTERNAL_SERVER_ERROR, e.getMessage()));
                    }
                });
    }
}
