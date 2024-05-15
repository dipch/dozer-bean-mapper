package com.xyz.beanmappermiddleware.auth;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthException extends RuntimeException {
    public HttpStatus code;
    public String message;
}