package com.xyz.beanmappermiddleware.config.routes;

import com.xyz.beanmappermiddleware.adapter.in.web.handlers.STTMiddlewareHandler;
import com.xyz.beanmappermiddleware.adapter.in.web.handlers.BeanMapperMiddlewareHandler;
import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.core.utils.CommonFunctions;
import com.xyz.beanmappermiddleware.core.utils.enums.HeaderNames;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.xyz.beanmappermiddleware.config.constants.Constants.AUTHORIZATION_REQUIRED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class BeanMapperMiddlewareRouteConfig {
    @Bean
    public RouterFunction<ServerResponse> beanMappingMiddlewareRoutes(BeanMapperMiddlewareHandler handler, STTMiddlewareHandler STTMiddlewareHandler){
        return route()
                .nest(RequestPredicates.accept(MediaType.APPLICATION_JSON), nestBuilder ->
                        nestBuilder.POST(RouteNames.BEANMAPPING_MIDDLEWARE_BASE_URL.getValue(), handler::handleRequest)
                                .before(this::headersSanityCheck))
                .nest(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA), nestBuilder ->
                        nestBuilder.POST(RouteNames.speech_to_text_MIDDLEWARE_BASE_URL.getValue(), STTMiddlewareHandler::getSpeechToTextMiddleWareResponse).before(this::headersSanityCheck))
                .build();
    }

    private ServerRequest headersSanityCheck(ServerRequest request) {
        CommonFunctions.validateHeaders(request, HeaderNames.AUTHORIZATION.getValue(),AUTHORIZATION_REQUIRED);
        return request;
    }

}