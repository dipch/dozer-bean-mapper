package com.xyz.beanmappermiddleware.config.helpers;

import java.util.*;

public class TypeMappings {
    private static final Map<List<String>, String> chatbotTypeToMiddlewareType = initchatbotTypeToMiddlewareTypeMap();
    private static Map<List<String>, String> initchatbotTypeToMiddlewareTypeMap() {
        Map<List<String>, String> map = new HashMap<>();
        map.put(new ArrayList<>(List.of("api_error")), "apiError");
        map.put(new ArrayList<>(List.of("loop_start")), "loopStart");
        map.put(new ArrayList<>(List.of("beneficiary_list")), "beneficiaryList");
        map.put(new ArrayList<>(List.of("account_selection")), "accountSelection");
        map.put(new ArrayList<>(List.of("beneficiary_selection")), "beneficiarySelection");
        map.put(new ArrayList<>(List.of("beneficiary_list_empty")), "beneficiaryListEmpty");
        map.put(new ArrayList<>(List.of("ft_amount_selection")), "ftAmountSelection");
        map.put(new ArrayList<>(List.of("generic", "ft_info", "confirmation_button")), "ftInfo");
        map.put(new ArrayList<>(List.of("generic", "generic")), "otpVerify");
        map.put(new ArrayList<>(List.of("generic", "ft_input_otp")), "ftInputOtp");
        map.put(new ArrayList<>(List.of("otp_incorrect", "ft_input_otp")), "ftIncorrectOtp");
        map.put(new ArrayList<>(List.of("prev_otp_alive", "ft_input_otp")), "ftOtpActive");
        map.put(new ArrayList<>(List.of("generic", "loop_complete")), "balanceEnquiryLoopComplete");
        map.put(new ArrayList<>(List.of("generic", "ft_success_info", "loop_complete")), "ftSuccessInfoLoopComplete");
        map.put(new ArrayList<>(List.of("generic")), "genericInfo");
        map.put(new ArrayList<>(List.of("generic", "beneficiary_selection")), "retryBeneficiarySelection");
        map.put(new ArrayList<>(List.of("generic", "account_selection")), "retryAccountSelection");
        map.put(new ArrayList<>(List.of("generic", "ft_amount_selection")), "retryFtAmountSelection");
        map.put(new ArrayList<>(List.of("generic","generic","ft_info", "confirmation_button")), "retryFtConfirmation");
        map.put(new ArrayList<>(List.of("generic","generic","account_selection")), "accountBeneficiaryNotMatched");
        map.put(new ArrayList<>(List.of("generic","generic","generic","account_selection")), "accountBeneficiaryAmountNotMatched");
        map.put(new ArrayList<>(List.of("generic","generic","ft_amount_selection")), "accountBeneficiaryNotMatchedAmountSelection");
        map.put(new ArrayList<>(List.of("generic","generic","generic","ft_amount_selection")), "accountBeneficiaryAmountNotMatchedAmountSelection");

        return Collections.unmodifiableMap(map);
    }



    private static final Map<String, String> middlewareTypeToHeader = initMiddlewareTypeToHeaderMap();
    private static Map<String, String> initMiddlewareTypeToHeaderMap() {
        Map<String, String> map = new HashMap<>();
        map.put("accountBeneficiaryAmountNotMatchedAmountSelection", "accountBeneficiaryAmountNotMatchedAmountSelection");
        map.put("accountBeneficiaryNotMatchedAmountSelection", "accountBeneficiaryNotMatchedAmountSelection");
        map.put("api_error", "apiError");
        map.put("apiError", "apiError");
        map.put("beneficiary_list", "beneficiaryList");
        map.put("beneficiary_list_empty", "beneficiaryListEmpty");
        map.put("account_selection", "accountSelection");
        map.put("beneficiary_selection", "beneficiarySelection");
        map.put("ft_amount_selection", "ftAmountSelection");
        map.put("confirmation_button", "ftInfo");
        map.put("generic", "genericInfo");
        map.put("otpVerify", "otpVerify");
        map.put("ft_input_otp", "ftInputOtp");
        map.put("ftInputOtp", "ftInputOtp");
        map.put("ft_success_info", "ftSuccessInfo");
        map.put("balanceEnquiryLoopComplete", "balanceEnquiryLoopComplete");
        map.put("ftSuccessInfoLoopComplete", "ftSuccessInfoLoopComplete");
        map.put("retryAccountSelection", "retryAccountSelection");
        map.put("retryBeneficiarySelection", "retryBeneficiarySelection");
        map.put("retryFtAmountSelection", "retryFtAmountSelection");
        map.put("retryFtConfirmation", "retryFtConfirmation");
        map.put("loop_start", "loopStart");
        map.put("ftIncorrectOtp", "ftIncorrectOtp");
        map.put("ftOtpActive", "ftOtpActive");
        map.put("accountBeneficiaryNotMatched", "accountBeneficiaryNotMatched");
        map.put("accountBeneficiaryAmountNotMatched", "accountBeneficiaryAmountNotMatched");

        return Collections.unmodifiableMap(map);
    }


    private static final Map<String, List<String>> middlewareHeaderToSetPropertyList = initMiddlewareHeaderToSetPropertyListMap();
    private static Map<String, List<String>> initMiddlewareHeaderToSetPropertyListMap() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("apiError", new ArrayList<>(List.of("title", "type", "path")));
        map.put("beneficiaryList", new ArrayList<>(List.of("type", "retryText")));
        map.put("accountBeneficiaryAmountNotMatchedAmountSelection", new ArrayList<>(List.of("type", "accounts","beneficiaryList", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "loopCompleteText","activeOtpText","accounts","retryText")));
        map.put("accountBeneficiaryNotMatchedAmountSelection", new ArrayList<>(List.of("type","accounts", "beneficiaryList", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "loopCompleteText","activeOtpText","accounts","retryText", "amountMatchingErrorText", "incorrectOtpText")));
        map.put("accountSelection", new ArrayList<>(List.of("type", "amountList","retryText", "beneficiaryList", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "beneficiaryList", "loopCompleteText","incorrectOtpText","activeOtpText", "beneficiaryMatchingErrorText", "accountMatchingErrorText", "amountMatchingErrorText")));
        map.put("genericInfo", new ArrayList<>(List.of("type", "title", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "loopCompleteText", "incorrectOtpText","activeOtpText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("beneficiarySelection", new ArrayList<>(List.of("type", "amountList","retryText", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "amountList", "accounts", "loopCompleteText","incorrectOtpText","activeOtpText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("ftAmountSelection", new ArrayList<>(List.of("type", "retryText", "title", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "beneficiaryList", "accounts", "loopCompleteText","incorrectOtpText","activeOtpText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("ftInfo", new ArrayList<>(List.of("type","genericText", "retryText", "title", "beneficiaryList", "accounts", "amountList", "inputOtpText", "sendOtpText", "loopCompleteText","incorrectOtpText","activeOtpText","beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("otpVerify", new ArrayList<>(List.of("type", "retryText", "verifyInfoText", "confirmationText","genericText", "title", "loopCompleteText","incorrectOtpText","activeOtpText","beneficiaryListEmptyText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("ftInputOtp", new ArrayList<>(List.of("type", "retryText", "verifyInfoText", "confirmationText", "genericText", "title", "amountList", "accounts", "beneficiaryList", "loopCompleteText","incorrectOtpText","activeOtpText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("ftIncorrectOtp", new ArrayList<>(List.of("type", "retryText", "verifyInfoText", "confirmationText", "title", "amountList", "accounts","sendOtpText","genericText", "inputOtpText", "beneficiaryList", "loopCompleteText","activeOtpText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("ftOtpActive", new ArrayList<>(List.of("type", "retryText", "verifyInfoText", "confirmationText", "title", "amountList", "accounts","sendOtpText","genericText", "inputOtpText", "beneficiaryList", "loopCompleteText","incorrectOtpText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("ftSuccessInfo", new ArrayList<>(List.of("type", "retryText", "verifyInfoText", "confirmationText","inputOtpText", "sendOtpText", "loopCompleteText","incorrectOtpText","activeOtpText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("ftSuccessInfoLoopComplete", new ArrayList<>(List.of("type", "retryText", "verifyInfoText", "confirmationText","inputOtpText", "sendOtpText","incorrectOtpText","activeOtpText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("balanceEnquiryLoopComplete", new ArrayList<>(List.of("type", "title", "retryText", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText","incorrectOtpText","activeOtpText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("retryFtConfirmation", new ArrayList<>(List.of("type","genericText", "title", "beneficiaryList", "accounts", "amountList", "inputOtpText", "sendOtpText", "loopCompleteText","incorrectOtpText","activeOtpText","beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("retryBeneficiarySelection", new ArrayList<>(List.of("incorrectOtpText","type", "accounts", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "amountList", "loopCompleteText","activeOtpText", "beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("retryAccountSelection", new ArrayList<>(List.of("incorrectOtpText","type", "beneficiaryList", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "amountList", "loopCompleteText","activeOtpText","beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("retryFtAmountSelection", new ArrayList<>(List.of("type", "accounts", "beneficiaryList", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "loopCompleteText","activeOtpText","beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("loopStart", new ArrayList<>(List.of("type","retryText","accounts", "beneficiaryList", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "loopCompleteText","incorrectOtpText","activeOtpText","beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("beneficiaryListEmpty", new ArrayList<>(List.of("type","retryText","accounts", "beneficiaryList", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "loopCompleteText","incorrectOtpText","activeOtpText","beneficiaryMatchingErrorText","accountMatchingErrorText","amountMatchingErrorText")));
        map.put("accountBeneficiaryAmountNotMatched", new ArrayList<>(List.of("type", "beneficiaryList", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "amountList", "loopCompleteText","activeOtpText","retryText")));
        map.put("accountBeneficiaryNotMatched", new ArrayList<>(List.of("type", "beneficiaryList", "verifyInfoText", "confirmationText", "sendOtpText", "inputOtpText", "amountList", "loopCompleteText","activeOtpText","retryText","amountMatchingErrorText", "title")));

        return Collections.unmodifiableMap(map);
    }



    private static final Map<String, String> middlewareHeaderToSetTitle = initMiddlewareHeaderToSetTitleMap();
    private static Map<String, String> initMiddlewareHeaderToSetTitleMap() {
        Map<String, String> map = new HashMap<>();

        map.put("beneficiaryList","beneficiary list");
        map.put("accountSelection", "account select");
        map.put("beneficiarySelection", "beneficiary select");
        map.put("ftSuccessInfo", "transaction successful");
        map.put("ftSuccessInfoLoopComplete", "transaction successful");
        map.put("retryAccountSelection", "account select");
        map.put("retryBeneficiarySelection", "beneficiary select");
        return Collections.unmodifiableMap(map);
    }


    public static String getMiddlewareResponseHeaderType(String middlewareType) {
        return middlewareTypeToHeader.get(middlewareType);
    }

    public static String getMiddlewareType(List<String> chatbotResponseTypeList) {
        return chatbotTypeToMiddlewareType.get(chatbotResponseTypeList);
    }

    public static List<String> getSetMiddlewareResponsePropertyList(String header) {
        return middlewareHeaderToSetPropertyList.get(header);
    }

    public static String getSetMiddlewareResponseTitle(String header) {
        return middlewareHeaderToSetTitle.get(header);
    }




}
