package com.xyz.beanmappermiddleware.core.validator;


import com.xyz.beanmappermiddleware.application.port.in.dto.request.MiddlewareRequestDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class MiddlewareRequestDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MiddlewareRequestDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sender", "400", "sender cannot be null or empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accessToken", "400", "accessToken cannot be null or empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message", "400", "message cannot be null or empty");

    }

}
