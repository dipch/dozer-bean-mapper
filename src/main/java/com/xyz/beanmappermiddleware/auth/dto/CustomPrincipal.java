package com.xyz.beanmappermiddleware.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal {
    List<Authority> authorities;
    private String name;


}