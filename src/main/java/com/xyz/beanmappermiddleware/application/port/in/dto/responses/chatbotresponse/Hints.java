package com.xyz.beanmappermiddleware.application.port.in.dto.responses.chatbotresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hints {
    private List<String> items;
    private List<Buttons> buttons;
}

