package com.xyz.beanmappermiddleware.application.port.in.dto.responses.chatbotresponse;

import lombok.*;
import org.dozer.Mapping;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ChatbotResponseDTO{


    @Mapping("recipientId")
    private String recipient_id;
    @Mapping("code")
    public String getCode(){
        return this.custom.getValues().getCode();
    }
    @Mapping("error")
    public String getError(){
        return this.custom.getValues().getError();
    }
    @Mapping("errorDescription")
    public String getErrorDescription(){
        return this.custom.getValues().getError_description();
    }
    @Mapping("timestamp")
    public String getTimestamp(){
        return this.custom.getValues().getTimestamp();
    }
    @Mapping("status")
    public String getStatus(){
        return this.custom.getValues().getStatus();
    }
    @Mapping("message")
    public String getMessage(){
        return this.custom.getValues().getMessage();
    }
    @Mapping("path")
    public String getPath(){
        return this.custom.getValues().getPath();
    }
    @Mapping("title")
    public String getTitle(){
        return this.custom.getType();
    }
    @Mapping("beneficiaryList")
    public List<Buttons> getBeneficiaryList(){
        return this.custom.getValues().getButtons() == null ? this.custom.getValues().getBeneficiary_list() : this.custom.getValues().getButtons();
    }
    @Mapping("genericText")
    public String getGenericText(){
        return this.custom.getValues().getText();
    }
    @Mapping("accountMatchingErrorText")
    public String getAccountMatchingErrorText(){
        return this.custom.getValues().getText();
    }
    @Mapping("beneficiaryMatchingErrorText")
    public String getBeneficiaryMatchingErrorText(){
        return this.custom.getValues().getText();
    }
    @Mapping("amountMatchingErrorText")
    public String getAmountMatchingErrorText(){
        return this.custom.getValues().getText();
    }
    @Mapping("loopCompleteText")
    public String getLoopCompleteText(){
        return this.custom.getValues().getText();
    }

    @Mapping("incorrectOtpText")
    public String getIncorrectOtpText(){
        return this.custom.getValues().getText();
    }

    @Mapping("activeOtpText")
    public String getActiveOtpText(){
        return this.custom.getValues().getText();
    }
    @Mapping("retryText")
    public String getRetryText(){
        return this.custom.getValues().getText();
    }
    @Mapping("accounts")
    public List<Buttons> getAccounts() {
        return this.custom.getValues().getButtons();
    }
    @Mapping("amountList")
    public List<Buttons> getAmountList() {
        return this.custom.getValues().getButtons();
    }
    @Mapping("verifyInfoText")
    public String getVerifyInfoText() {
        return this.custom.getValues().getText();
    }
    @Mapping("fromAccount")
    public String getFromAccount(){
        return this.custom.getValues().getFrom_account();
    }
    @Mapping("toAccount")
    public String getToAccount(){
        return this.custom.getValues().getTo_account();
    }
    @Mapping("transferAmount")
    public String getTransferAmount(){
        return this.custom.getValues().getTransfer_amount();
    }
    @Mapping("confirmationText")
    public String getConfirmationText() {
        return this.custom.getValues().getText();
    }
    @Mapping("sendOTPText")
    public String getSendOTPText() { return this.custom.getValues().getText();}
    @Mapping("inputOTPText")
    public String getInputOTPText(){
        return this.custom.getValues().getText();
    }
    @Mapping("fromAccountType")
    public String getFromAccountType(){
        return this.custom.getValues().getFrom_account_type();
    }
    @Mapping("toAccountType")
    public String getToAccountType(){
        return this.custom.getValues().getTo_account_type();
    }
    @Mapping("fromAccountHolderName")
    public String getFromAccountHolderName(){
        return this.custom.getValues().getFrom_account_holder_name();
    }
    @Mapping("toAccountHolderName")
    public String getToAccountHolderName(){
        return this.custom.getValues().getTo_account_holder_name();
    }
    @Mapping("fromAccountBranchName")
    public String getFromAccountBranchName(){
        return this.custom.getValues().getFrom_account_branch_name();
    }
    @Mapping("toAccountBranchName")
    public String getToAccountBranchName(){
        return this.custom.getValues().getTo_account_branch_name();
    }
    @Mapping("fromAccountNickName")
    public String getFromAccountNickName(){
        return this.custom.getValues().getFrom_account_name();
    }
    @Mapping("toAccountNickName")
    public String getToAccountNickName(){
        return this.custom.getValues().getTo_account_name();
    }

    @Mapping("vat")
    public String getVat(){
        return this.custom.getValues().getVat();
    }
    @Mapping("charge")
    public String getCharge(){
        return this.custom.getValues().getCharge();
    }
    @Mapping("grandTotal")
    public String getGrandTotal(){
        return this.custom.getValues().getGrand_total();
    }
    @Mapping("referenceNo")
    public String getReferenceNo(){
        return this.custom.getValues().getReference_no();
    }
    @Mapping("instructionsHintText")
    public List<String> getInstructionsHintText(){
        return this.custom.getHints() != null ? this.custom.getHints().getItems(): null;
    }
    @Mapping("instructionsButtonList")
    public List<Buttons> getInstructionsButtonList(){
        return this.custom.getHints() != null ? this.custom.getHints().getButtons() : null;
    }
    private Custom custom;
    @Mapping("type")
    public String getType(){
        return this.custom.getType();
    }
}
