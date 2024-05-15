package com.xyz.beanmappermiddleware.core.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TracerUtil {
    private final Tracer tracer;
    public TracerUtil(Tracer tracer) {
        this.tracer = tracer;
    }

    public String getCurrentTraceId() {
        Span span = tracer.currentSpan();
        if (span == null) {
            log.info("Sleuth Span is Null !");
            return "";
        }
        return span.context().traceId();

    }
}