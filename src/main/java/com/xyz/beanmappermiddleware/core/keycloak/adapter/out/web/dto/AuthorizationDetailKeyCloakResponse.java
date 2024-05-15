package com.xyz.beanmappermiddleware.core.keycloak.adapter.out.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.xyz.beanmappermiddleware.core.utils.CommonFunctions;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthorizationDetailKeyCloakResponse {

    private String access_token;
    private Long expires_in;
    private Long refresh_expires_in;
    private String token_type;
    private Integer not_before_policy;
    private String scope;


    @Override
    public String toString() {
        return CommonFunctions.buildGsonBuilder(this);
    }
}

