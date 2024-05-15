package com.xyz.beanmappermiddleware.application.port.in.dto.responses.speechtotext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.xyz.beanmappermiddleware.core.utils.CommonFunctions;
import org.dozer.Mapping;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpeechToTextResponse {
    @Mapping("text")
    private String text;

    @Mapping("processingTime")
    private double processingTime;

    @Mapping("similarity")
    private String similarity;
    private String timeStamp;

    @Override
    public String toString() {
        return CommonFunctions.buildGsonBuilder(this);
    }
}
