package com.xyz.beanmappermiddleware.application.port.out;

import com.xyz.beanmappermiddleware.application.port.in.dto.request.MiddlewareRequestDTO;
import com.xyz.beanmappermiddleware.application.port.in.dto.responses.chatbotresponse.ChatbotResponseDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BeanMapperMiddlewarePort {
    Mono<List<ChatbotResponseDTO>> getChatbotResponse(MiddlewareRequestDTO requestDTO);
}
