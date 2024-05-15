package com.xyz.beanmappermiddleware.auth;

import com.xyz.beanmappermiddleware.auth.dto.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenAuthorizationResponse {

    private List<Authority> authorities;
    private String name;
}
