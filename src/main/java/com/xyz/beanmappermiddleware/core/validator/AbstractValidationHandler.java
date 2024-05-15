package com.xyz.beanmappermiddleware.core.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractValidationHandler<T, U extends Validator> {

    private final Class<T> validationClass;

    private final U validator;

    protected AbstractValidationHandler(Class<T> clazz, U validator) {
        this.validationClass = clazz;
        this.validator = validator;
    }

    abstract protected Mono<ServerResponse> processBody(T validBody, final ServerRequest originalRequest);

    public final Mono<ServerResponse> handleRequest(final ServerRequest request) {
        return request.bodyToMono(this.validationClass)
                .flatMap(body -> {
                    log.info("body is to be validate:{}",body);
                    Errors errors = new BeanPropertyBindingResult(body, this.validationClass.getName());
                    this.validator.validate(body, errors);
                    log.info("errors : {}", errors.getErrorCount());
                    if (errors.getAllErrors()
                            .isEmpty()) {
                        return processBody(body, request);
                    } else {
                        log.info("errors : {}", errors.getErrorCount());
                        return onValidationErrors(errors, body, request);
                    }
                });
    }

    protected Mono<ServerResponse> onValidationErrors(Errors errors, T invalidBody, final ServerRequest request) {
      String  errormessageTemplate = errors.getAllErrors().stream()
                .map( objectError ->{
                    String defaultMessage = objectError.getDefaultMessage();
                    log.error("errorMessages : {}",defaultMessage);
                    return defaultMessage;
                })
                .collect(Collectors.joining(","));
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errormessageTemplate);
    }


}