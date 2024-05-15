package com.xyz.beanmappermiddleware.application.port.in.dto.responses.chatbotresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Custom {
    private Values values;
    private String type;
    private Hints hints;

}
