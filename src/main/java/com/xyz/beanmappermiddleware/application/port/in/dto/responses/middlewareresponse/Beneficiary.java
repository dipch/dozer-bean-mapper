package com.xyz.beanmappermiddleware.application.port.in.dto.responses.middlewareresponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import com.xyz.beanmappermiddleware.core.utils.CommonFunctions;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Beneficiary {
    private String accountNo;
    private String name;
    private String accountTitle;
    private String branchName;
    private String accountId;
    private String beneficiaryOid;
    private String title;
    private String payload;

    @Override
    public String toString() {
        return CommonFunctions.buildGsonBuilder(this);
    }
}
