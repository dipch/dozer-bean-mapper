package com.xyz.beanmappermiddleware.config;

import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.core.utils.enums.HeaderNames;
import com.xyz.beanmappermiddleware.core.utils.TracerUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Configuration
@Slf4j
public class ChatbotWebClientConfig {
    private final HttpClient httpClient;

    @Value("${chatbot.apiurl}")
    private String chatbotBaseUrl;

    @Value("${speechtotext.apiurl}")
    private String speechToTextBaseUrl;
    @Value("${oauth2.baseUrl}")
    private String authUrl;

    @Value("${keycloak.base.url}")
    private String keyCloakBaseUrl;
    private final TracerUtil tracerUtil;


    public ChatbotWebClientConfig(@Qualifier("httpClientWithTimeout") HttpClient httpClient, TracerUtil tracerUtil) {
        this.httpClient = httpClient;
        this.tracerUtil = tracerUtil;
    }

    @Bean
    public WebClient webClientWithTimeout() {
        return getWebClient(chatbotBaseUrl, httpClient);
    }

    @Bean
    public WebClient keyCloakWebClient() {
        return getWebClient(keyCloakBaseUrl, httpClient);
    }

/*
 @Bean
    public WebClient speechToTextWebClient() {
        return getWebClient(speechToTextBaseUrl, httpClient);
    }
*/


    @Bean
    public WebClient authClient() {
        return getWebClient(authUrl, httpClient);
    }
    private WebClient getWebClient(String baseUrl, HttpClient httpClient) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter((ClientRequest request, ExchangeFunction next) -> {
                    ClientRequest updatedRequest = setRequestHeaders(request);
                    logRequest(updatedRequest);
                    return next.exchange(updatedRequest)
                            .doOnNext((ClientResponse response) -> {
                              //  setResponseHeaders(updatedRequest, response);
                                logResponse(response, baseUrl);
                            });
                })
                .build();
    }


    private ClientRequest setRequestHeaders(ClientRequest request) {
        String traceId = tracerUtil.getCurrentTraceId();
        if (Optional.ofNullable(request.headers().get(HeaderNames.TRACE_ID.getValue())).isPresent()
                && !Objects.requireNonNull(request.headers().get(HeaderNames.TRACE_ID.getValue()))
                .stream()
                .allMatch(String::isEmpty)) {
            log.info("traceId already exist in request");
            return ClientRequest.from(request)
                    .build();
        }
        return ClientRequest.from(request)
                .build();
    }

    private void setResponseHeaders(ClientRequest request, ClientResponse response) {
        response.mutate().header(HeaderNames.RESPONSE_RECEIVED_TIME_IN_MS.getValue(), String.valueOf(LocalDateTime.now()));
        if (!Objects.requireNonNull(request.headers().get(HeaderNames.REQUEST_SENT_TIME_IN_MS.getValue()))
                .stream()
                .allMatch(String::isEmpty)) {
            String s = Objects.requireNonNull(request.headers().get(HeaderNames.REQUEST_SENT_TIME_IN_MS.getValue()))
                    .stream()
                    .findAny().orElse(LocalDateTime.now().toString());
            response.mutate().header(HeaderNames.RESPONSE_TRANSMISSION_TIME_IN_MS.getValue(),
                    String.valueOf(
                            LocalDateTime.now()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                                    .toEpochMilli()
                                    - LocalDateTime
                                    .parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS"))
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                                    .toEpochMilli()
                    ));
        }
    }

    private void logRequest(ClientRequest request) {
        try {
            log.info("""
                            Request Sending From {}
                             Uri : {}
                             Method : {}
                             Headers : {}
                             Content type : {}
                             Acceptable Media Type {}
                            """,
                    InetAddress.getLocalHost().getHostAddress(),
                    request.url(),
                    request.method(),
                    request.headers(),
                    request.headers().getContentType(),
                    request.headers().getAccept());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private void logResponse(ClientResponse response, String baseUrl) {
        log.info("""
                        Response Receiving from {}
                         Headers : {}
                         Response Status : {}
                         Content type : {}
                        """,
                baseUrl,
                response.headers().asHttpHeaders(),
                response.statusCode(),
                response.headers().contentType()
        );
        log.info("Response receiving time from {} is {} ms", baseUrl,
                Objects.requireNonNull(response.headers()
                        .header(HeaderNames.RESPONSE_TRANSMISSION_TIME_IN_MS.getValue())
                        .stream()
                        .findFirst()
                        .orElse("0")
                )
        );
    }
}
