package com.xyz.beanmappermiddleware.core.keycloak.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthorizationDetail {

    private String accessToken;
    private Long expiresIn;
    private Long refreshExpiresIn;
    private String tokenType;
    private String scope;
    private Integer notBeforePolicy;
    private LocalDateTime createdOn;

}
