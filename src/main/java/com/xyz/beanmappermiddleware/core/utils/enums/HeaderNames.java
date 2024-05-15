package com.xyz.beanmappermiddleware.core.utils.enums;

import lombok.Getter;

@Getter
public enum HeaderNames {
    REQUEST_RECEIVED_TIME_IN_MS("Request-Received-Time-In-Ms"),
    REQUEST_TIMEOUT_TIME_IN_SEC("Request-Timeout-In-Seconds"),
    REQUEST_SENT_TIME_IN_MS("Request-Sent-Time-In-Ms"),
    RESPONSE_RECEIVED_TIME_IN_MS("Response-Received-Time-In-Ms"),
    RESPONSE_TRANSMISSION_TIME_IN_MS("Response-Transmission-Time-In-Ms"),
    RESPONSE_PROCESSING_TIME_IN_MS("Response-Processing-Time-In-Ms"),
    RESPONSE_SENT_TIME_IN_MS("Response-Sent-Time-In-Ms"),
    TRACE_ID("Trace-Id"),
    AUTHORIZATION("Authorization")

    ;

    private final String value;

    HeaderNames(String value) {
        this.value = value;
    }
}