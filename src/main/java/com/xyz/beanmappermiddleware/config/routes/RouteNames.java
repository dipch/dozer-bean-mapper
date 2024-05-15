package com.xyz.beanmappermiddleware.config.routes;

import lombok.Getter;

@Getter
public enum RouteNames {

    BEANMAPPING_MIDDLEWARE_BASE_URL("/api/v1/beanmapping"),
    speech_to_text_MIDDLEWARE_BASE_URL("/api/v1/speech-to-text"),

    ;


    private final String value;

    RouteNames(String value) {
        this.value = value;
    }
}
