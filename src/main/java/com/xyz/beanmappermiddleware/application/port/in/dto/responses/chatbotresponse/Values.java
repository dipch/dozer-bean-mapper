package com.xyz.beanmappermiddleware.application.port.in.dto.responses.chatbotresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dozer.Mapping;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Values {
    @Mapping("code")
    private String code;
    @Mapping("error")
    private String error;
    @Mapping("errorDescription")
    private String error_description;
    private String timestamp;
    private String status;
    private String message;
    private String path;
    private List<Buttons> buttons;
    private List<Buttons> beneficiary_list;
    private String text;
    @Mapping("fromAccount")
    private String from_account;
    @Mapping("toAccount")
    private String to_account;
    @Mapping("transferAmount")
    private String transfer_amount;
    @Mapping("fromAccountType")
    private String from_account_type;
    @Mapping("toAccountType")
    private String to_account_type;
    @Mapping("fromAccountNickName")
    private String from_account_name;
    @Mapping("toAccountNickName")
    private String to_account_name;
    @Mapping("fromAccountHolderName")
    private String from_account_holder_name;
    @Mapping("fromAccountBranchName")
    private String from_account_branch_name;
    @Mapping("toAccountHolderName")
    private String to_account_holder_name;
    @Mapping("toAccountBranchName")
    private String to_account_branch_name;
    @Mapping("vat")
    private String vat;
    @Mapping("charge")
    private String charge;
    @Mapping("grandTotal")
    private String grand_total;
    @Mapping("referenceNo")
    private String reference_no;

}
