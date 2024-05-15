package com.xyz.beanmappermiddleware.core.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
@Slf4j
public class CommonFunctions {

    public MultiValueMap<String, String> getHeadersConsumer(String token) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + token);
        headers.add(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON));
        return headers;
    }



    public String buildGsonBuilder(Object object) {
        return new GsonBuilder()
                .setExclusionStrategies(new GsonExcludeStrategy())
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) ->
                                new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))))
                .setPrettyPrinting().create().toJson(object);
    }


    public void validateHeaders(ServerRequest request, String header, String message) {
        if (request.headers().header(header).isEmpty()) {
            log.error("error occurred while checking headers: {}", header);
            throw new ServerWebInputException(message);
        }
        if (StringUtils.isBlank(request.headers().firstHeader(header))) {
            log.error("error occurred while checking headers: {}", header);
            throw new ServerWebInputException(message);
        }
    }

}
