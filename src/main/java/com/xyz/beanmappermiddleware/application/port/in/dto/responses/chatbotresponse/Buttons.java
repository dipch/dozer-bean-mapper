package com.xyz.beanmappermiddleware.application.port.in.dto.responses.chatbotresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dozer.Mapping;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Buttons {
    @Mapping("accountNo")
    private String account_no;
    @Mapping("name")
    private String name;
    @Mapping("accountTitle")
    private String account_title;
    @Mapping("branchName")
    private String branch_name;
    @Mapping("accountId")
    private String account_id;
    @Mapping("beneficiaryOid")
    private String beneficiary_oid;
    @Mapping("title")
    private String title;
    @Mapping("payload")
    private String payload;
}
