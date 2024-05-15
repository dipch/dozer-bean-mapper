package com.xyz.beanmappermiddleware.application.port.in.usecase;
import com.xyz.beanmappermiddleware.application.port.in.dto.responses.middlewareresponse.MiddlewareResponseDTO;
import com.xyz.beanmappermiddleware.application.port.in.dto.request.MiddlewareRequestDTO;
import reactor.core.publisher.Mono;

public interface BeanMapperMiddlewareUseCase {
    Mono<MiddlewareResponseDTO>  getMiddlewareResponse(MiddlewareRequestDTO middlewareRequestDTOMono, String userId);
    MiddlewareResponseDTO setMiddlewareResponseDTOTitleAndNullProperties(MiddlewareResponseDTO middlewareResponseDTO, String header);

    Boolean checkIfChatbotResponseBodyContainsErrorCode401(MiddlewareResponseDTO middlewareResponseDTO);

    Boolean checkIfChatbotResponseBodyContainsErrorCode423(MiddlewareResponseDTO middlewareResponseDTO);
}
