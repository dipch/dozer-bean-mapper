package com.xyz.beanmappermiddleware.application.port.in.dto.responses.middlewareresponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import com.xyz.beanmappermiddleware.core.utils.CommonFunctions;
import com.xyz.beanmappermiddleware.core.utils.GsonExclude;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MiddlewareResponseDTO {
    private String recipientId;
    private String code;
    private String error;
    private String errorDescription;
    private String timestamp;
    private String status;
    private String message;
    private String path;
    private String retryText;
    private String accountMatchingErrorText;
    private String beneficiaryMatchingErrorText;
    private String amountMatchingErrorText;
    private String genericText;

    private String title;

    @GsonExclude
    private List<Beneficiary> beneficiaryList;

    @GsonExclude
    private List<Account> accounts;

    @GsonExclude
    private List<Button> amountList;
    private String verifyInfoText;
    private String fromAccount;
    private String fromAccountNickName;
    private String fromAccountType;
    private String fromAccountHolderName;
    private String fromAccountBranchName;
    private String toAccount;
    private String toAccountNickName;
    private String toAccountType;
    private String toAccountHolderName;
    private String toAccountBranchName;
    private String transferAmount;
    private String currency;
    private String confirmationText;
    private Button yesButton;
    private Button noButton;
    private String sendOtpText;
    private String inputOtpText;
    private Button resendOtpButton;
    private String vat;
    private String charge;
    private String grandTotal;
    private String referenceNo;
    private String type;

    @GsonExclude
    private List<String> instructionsHintText;

    @GsonExclude
    private List<Button> instructionsButtonList;
    private String loopCompleteText;
    private String incorrectOtpText;
    private String activeOtpText;


    @Override
    public String toString() {
        return CommonFunctions.buildGsonBuilder(this);
    }
}
