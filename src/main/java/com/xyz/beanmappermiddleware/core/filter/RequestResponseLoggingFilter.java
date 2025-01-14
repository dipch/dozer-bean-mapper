package com.xyz.beanmappermiddleware.core.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.min;


@Component
@Slf4j
public class RequestResponseLoggingFilter  implements ExchangeFilterFunction {

    private static final int MAX_BYTES_LOGGED = 4_096;


    @Override
    @NonNull
    public Mono<ClientResponse> filter(@NonNull ClientRequest request, @NonNull ExchangeFunction next) {
//        if (!log.isDebugEnabled()) {
//            return next.exchange(request);
//        }

        var requestLogged = new AtomicBoolean(false);
        var responseLogged = new AtomicBoolean(false);

        var capturedRequestBody = new StringBuilder();
        var capturedResponseBody = new StringBuilder();

        var stopWatch = new StopWatch();
        stopWatch.start();

        return next
                .exchange(ClientRequest.from(request).body(new BodyInserter<>() {

                    @Override
                    @NonNull
                    public Mono<Void> insert(@NonNull ClientHttpRequest req, @NonNull Context context) {
                        return request.body().insert(new ClientHttpRequestDecorator(req) {

                            @Override
                            @NonNull
                            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                                return super.writeWith(Flux.from(body).doOnNext(data -> capturedRequestBody.append(extractBytes(data)))); // number of bytes appended is maxed in real code
                            }

                        }, context);
                    }
                }).build())
                .doOnNext(response -> {
                            if (!requestLogged.getAndSet(true)) {

                                log.info("""
                                                | >>---> Outgoing {} request
                                                 URL: {}
                                                 clientRequestBody: {}
                                                                                               
                                                 """,
                                        request.method(),
                                        request.url(),
                                        capturedRequestBody);

                            }
                        }
                )
                .doOnError(error -> {
                    if (!requestLogged.getAndSet(true)) {
                        log.info("""    
                                        | >>---> Outgoing {} request
                                        URL: {}
                                        Error: {}
                                                                              
                                        """,
                                request.method(),
                                request.url(),
                                error.getMessage()
                        );
                    }
                })
                .map(response -> response.mutate().body(transformer -> transformer
                                .doOnNext(body -> capturedResponseBody.append(extractBytes(body))) // number of bytes appended is maxed in real code
                                .doOnTerminate(() -> {
                                    if (stopWatch.isRunning()) {
                                        stopWatch.stop();
                                    }
                                })
                                .doOnComplete(() -> {
                                    if (!responseLogged.getAndSet(true)) {
                                        log.debug("""
                                                        | <---<< Response for outgoing {} request
                                                        URL: {}
                                                        clientRequestExecutionTimeInMillis: {}
                                                        clientResponseStatusCode: {}                        
                                                        clientResponseHeaders: {}
                                                        clientResponseBody: {}  
                                                                                                       
                                                        """,
                                                request.method(),
                                                request.url(),
                                                stopWatch.getTotalTimeMillis(),
                                                response.statusCode().value(),
                                                response.headers(),
                                                capturedResponseBody
                                        );
                                    }
                                })
                                .doOnError(error -> {
                                            if (!responseLogged.getAndSet(true)) {
                                                log.debug("| <---<< Error parsing response for outgoing {} request URL{} after {}ms\n{}",
                                                        request.method(),
                                                        request.url(),
                                                        stopWatch.getTotalTimeMillis(),
                                                        error.getMessage()
                                                );
                                            }
                                        }
                                )
                        ).build()
                );
    }

    private static String extractBytes(DataBuffer data) {
        int currentReadPosition = data.readPosition();
        var numberOfBytesLogged = min(data.readableByteCount(), MAX_BYTES_LOGGED);
        var bytes = new byte[numberOfBytesLogged];
        data.read(bytes, 0, numberOfBytesLogged);
        data.readPosition(currentReadPosition);
        return new String(bytes);
    }


}