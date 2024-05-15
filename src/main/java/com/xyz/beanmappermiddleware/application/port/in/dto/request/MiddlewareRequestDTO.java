package com.xyz.beanmappermiddleware.application.port.in.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.xyz.beanmappermiddleware.core.utils.CommonFunctions;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MiddlewareRequestDTO {
    private String sender;
    private String accessToken;
    private String message;
    @Override
    public String toString() {
        return CommonFunctions.buildGsonBuilder(this);
    }

}