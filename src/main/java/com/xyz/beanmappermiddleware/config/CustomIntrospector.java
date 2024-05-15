package com.xyz.beanmappermiddleware.config;

import com.xyz.beanmappermiddleware.auth.dto.CustomPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Component
public  class CustomIntrospector implements ReactiveOpaqueTokenIntrospector {
    private final WebClient webClient;

    @Value("${oauth2.resource.userInfoUri}")
    private String authUrl;

    public CustomIntrospector(@Qualifier("authClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
        Map<String, Object> uriParam = new HashMap<>();
        uriParam.put("USER_ID", "");

        return webClient
                .get()
                .uri(authUrl)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .bodyToMono(CustomPrincipal.class)
                .doOnRequest(r -> log.info("Getting auth response for: {}", token))
                .doOnError(throwable ->  log.error("Failed to validate access token from auth server : ", throwable))
                .doOnSuccess(response -> log.info("Successfully get response from auth server : {}", response))
                .onErrorMap(throwable -> new InsufficientAuthenticationException(throwable.getMessage()))
                .map(customPrincipal -> new DefaultOAuth2AuthenticatedPrincipal(
                        customPrincipal.getName(), uriParam, extractAuthorities(customPrincipal)));
    }

    private Collection<GrantedAuthority> extractAuthorities(CustomPrincipal principal) {
        return principal.getAuthorities().stream()
                .map(authorities -> new SimpleGrantedAuthority(authorities.getAuthority()))
                .collect(Collectors.toList());
    }
}