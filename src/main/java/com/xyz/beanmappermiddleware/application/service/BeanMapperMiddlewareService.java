package com.xyz.beanmappermiddleware.application.service;

import com.xyz.beanmappermiddleware.application.port.in.dto.responses.middlewareresponse.MiddlewareResponseDTO;
import com.xyz.beanmappermiddleware.application.port.in.usecase.BeanMapperMiddlewareUseCase;
import com.xyz.beanmappermiddleware.application.port.out.BeanMapperMiddlewarePort;
import com.xyz.beanmappermiddleware.application.service.exception.InvalidChatbotResponseException;
import lombok.extern.slf4j.Slf4j;
import com.xyz.beanmappermiddleware.application.port.in.dto.request.MiddlewareRequestDTO;
import com.xyz.beanmappermiddleware.config.constants.Constants;
import com.xyz.beanmappermiddleware.config.helpers.TypeMappings;
import com.xyz.beanmappermiddleware.core.utils.NullAwareBeanUtilsBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
@Slf4j
public class BeanMapperMiddlewareService implements BeanMapperMiddlewareUseCase {
    private final BeanMapperMiddlewarePort beanMapperMiddlewarePort;


    public BeanMapperMiddlewareService(BeanMapperMiddlewarePort beanMapperMiddlewarePort) {
        this.beanMapperMiddlewarePort = beanMapperMiddlewarePort;
    }

    public Mono<MiddlewareResponseDTO> getMiddlewareResponse(MiddlewareRequestDTO requestDTO, String userId) throws InvalidChatbotResponseException {
        return beanMapperMiddlewarePort
                .getChatbotResponse(requestDTO)
                .flatMap(response -> {
                            List<MiddlewareResponseDTO> middlewareResponseDTOList = new ArrayList<>();
                            Mapper mapper = new DozerBeanMapper();
                            MiddlewareResponseDTO middlewareResponseDTOFinal = new MiddlewareResponseDTO();
                            List<String> responseTypeList = new ArrayList<>();
                            BeanUtilsBean notNull = new NullAwareBeanUtilsBean();

                            response.forEach(chatbotResponseDTO -> {
                                        MiddlewareResponseDTO middlewareResponseDTO = mapper.map(chatbotResponseDTO, MiddlewareResponseDTO.class);
                                        middlewareResponseDTO.setTimestamp(LocalDateTime.now().toString());
                                        responseTypeList.add(middlewareResponseDTO.getType());
                                        middlewareResponseDTOList.add(middlewareResponseDTO);
                                        try {
                                            notNull.copyProperties(middlewareResponseDTOFinal, middlewareResponseDTO);
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            throw new InvalidChatbotResponseException(500, Constants.FIELD_ERROR_INTERNAL_SERVER_ERROR, e.getMessage());
                                        }
                                    }
                            );
                            return Mono.just(setMiddlewareResponseDTOProperties(middlewareResponseDTOFinal, middlewareResponseDTOList));
                        }
                )
                .doOnRequest(r -> log.info("requested with userId: {}, for sessionId:{}", userId, requestDTO.getSender()))
                .onErrorResume(Predicate.not(InvalidChatbotResponseException.class::isInstance), e -> Mono.error(new InvalidChatbotResponseException(500, Constants.FIELD_ERROR_INTERNAL_SERVER_ERROR, e.getMessage())));
    }

    @Override
    public Boolean checkIfChatbotResponseBodyContainsErrorCode401(MiddlewareResponseDTO middlewareResponseDTO) {
        return (middlewareResponseDTO.getCode() != null && middlewareResponseDTO.getCode().equals("401"));
    }

    @Override
    public Boolean checkIfChatbotResponseBodyContainsErrorCode423(MiddlewareResponseDTO middlewareResponseDTO) {
        return (middlewareResponseDTO.getCode() != null && middlewareResponseDTO.getCode().equals("423"));
    }

    @Override
    public MiddlewareResponseDTO setMiddlewareResponseDTOTitleAndNullProperties(MiddlewareResponseDTO middlewareResponseDTO, String headerType) throws InvalidChatbotResponseException {
        List<String> propertyNameList = TypeMappings.getSetMiddlewareResponsePropertyList(headerType);
        try {
            if (headerType.equals(Constants.HEADER_ftSuccessInfo)) {
                middlewareResponseDTO.setCurrency(Constants.FIELD_CURRENCY_BDT);
                middlewareResponseDTO.setTitle(TypeMappings.getSetMiddlewareResponseTitle(headerType));
            }

            if (headerType.equals(Constants.HEADER_beneficiaryList)) {
                middlewareResponseDTO.setTitle(TypeMappings.getSetMiddlewareResponseTitle(headerType));
            }

            if (headerType.equals(Constants.HEADER_beneficiarySelection)) {
                middlewareResponseDTO.setTitle(TypeMappings.getSetMiddlewareResponseTitle(headerType));
            }

            if (headerType.equals(Constants.HEADER_accountSelection)) {
                middlewareResponseDTO.setTitle(TypeMappings.getSetMiddlewareResponseTitle(headerType));
            }

            if (headerType.equals(Constants.HEADER_retryAccountSelection)) {
                middlewareResponseDTO.setTitle(TypeMappings.getSetMiddlewareResponseTitle(headerType));
            }

            if (headerType.equals(Constants.HEADER_retryBeneficiarySelection)) {
                middlewareResponseDTO.setTitle(TypeMappings.getSetMiddlewareResponseTitle(headerType));
            }

            if (headerType.equals(Constants.HEADER_retryFtAmountSelection)) {
                middlewareResponseDTO.setTitle(TypeMappings.getSetMiddlewareResponseTitle(headerType));
            }


            propertyNameList.forEach(property -> {
                try {
                    BeanUtils.setProperty(middlewareResponseDTO, property, null);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InvalidChatbotResponseException(500, Constants.FIELD_ERROR_INTERNAL_SERVER_ERROR, "Error occurred while setting properties of Middleware Response");
                }
            });
        } catch (NullPointerException e) {
            throw new InvalidChatbotResponseException(500, Constants.FIELD_ERROR_INTERNAL_SERVER_ERROR, "Unrecognized header type received from chatbot response");
        } catch (Exception e) {
            throw new InvalidChatbotResponseException(500, Constants.FIELD_ERROR_INTERNAL_SERVER_ERROR, "Unrecognized response received from chatbot");
        }
        return middlewareResponseDTO;
    }


    public MiddlewareResponseDTO setMiddlewareResponseDTOProperties(MiddlewareResponseDTO middlewareResponseDTO, List<MiddlewareResponseDTO> middlewareResponseDTOList) {
        for (MiddlewareResponseDTO singleMiddlewareResponseDTO : middlewareResponseDTOList) {
            if (singleMiddlewareResponseDTO.getType().equals("api_error")) {
                middlewareResponseDTO.setType("apiError");
                if (middlewareResponseDTO.getMessage() != null) {
                    middlewareResponseDTO.setErrorDescription(middlewareResponseDTO.getMessage());
                }
            }
            if (middlewareResponseDTOList.size() > 1 && middlewareResponseDTO.getMessage() != null) {
                middlewareResponseDTO.setErrorDescription(middlewareResponseDTO.getMessage());
            }
        }

        if (middlewareResponseDTOList.size() == 1 && middlewareResponseDTOList.get(0).getType().equals("generic")) {
            middlewareResponseDTO.setType("generic");
        }

        if (middlewareResponseDTOList.size() == 2 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("loop_complete")) {
            middlewareResponseDTO.setType("balanceEnquiryLoopComplete");
            middlewareResponseDTO.setLoopCompleteText(middlewareResponseDTOList.get(1).getGenericText());
            middlewareResponseDTO.setGenericText(middlewareResponseDTOList.get(0).getGenericText());
        }

        if (middlewareResponseDTOList.size() == 2 && middlewareResponseDTOList.get(0).getType().equals("otp_incorrect") && middlewareResponseDTOList.get(1).getType().equals("ft_input_otp")) {
            middlewareResponseDTO.setType("ftIncorrectOtp");
            middlewareResponseDTO.setIncorrectOtpText(middlewareResponseDTOList.get(0).getIncorrectOtpText());
            middlewareResponseDTO.setResendOtpButton(middlewareResponseDTOList.get(1).getAmountList().get(0));

        }

        if (middlewareResponseDTOList.size() == 2 && middlewareResponseDTOList.get(0).getType().equals("prev_otp_alive") && middlewareResponseDTOList.get(1).getType().equals("ft_input_otp")) {
            middlewareResponseDTO.setType("ftOtpActive");
            middlewareResponseDTO.setActiveOtpText(middlewareResponseDTOList.get(0).getActiveOtpText());
            middlewareResponseDTO.setResendOtpButton(middlewareResponseDTOList.get(1).getAmountList().get(0));

        }

        if (middlewareResponseDTOList.size() == 2 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("ft_input_otp")) {
            middlewareResponseDTO.setType("ftInputOtp");
            middlewareResponseDTO.setSendOtpText(middlewareResponseDTOList.get(0).getGenericText());
            middlewareResponseDTO.setInputOtpText(middlewareResponseDTOList.get(1).getGenericText());
            middlewareResponseDTO.setResendOtpButton(middlewareResponseDTOList.get(1).getAmountList().get(0));
        }

        if (middlewareResponseDTOList.size() == 3 && middlewareResponseDTOList.get(2).getType().equals("confirmation_button")) {
            middlewareResponseDTO.setYesButton(middlewareResponseDTOList.get(2).getAmountList().get(0));
            middlewareResponseDTO.setNoButton(middlewareResponseDTOList.get(2).getAmountList().get(1));
            middlewareResponseDTO.setVerifyInfoText(middlewareResponseDTOList.get(0).getGenericText());
            middlewareResponseDTO.setConfirmationText(middlewareResponseDTOList.get(2).getGenericText());
        }

        if (middlewareResponseDTOList.size() == 2 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("account_selection")) {
            middlewareResponseDTO.setType("retryAccountSelection");
            middlewareResponseDTO.setRetryText(middlewareResponseDTOList.get(0).getGenericText());
        }
        if (middlewareResponseDTOList.size() == 3 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("account_selection")) {
            middlewareResponseDTO.setType("retryAccountSelection");
            middlewareResponseDTO.setRetryText(middlewareResponseDTOList.get(0).getGenericText());
        }

        if (middlewareResponseDTOList.size() == 2 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("beneficiary_selection")) {
            middlewareResponseDTO.setType("retryBeneficiarySelection");
            middlewareResponseDTO.setRetryText(middlewareResponseDTOList.get(0).getGenericText());
        }
        if (middlewareResponseDTOList.size() == 3 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("beneficiary_selection")) {
            middlewareResponseDTO.setType("retryBeneficiarySelection");
            middlewareResponseDTO.setRetryText(middlewareResponseDTOList.get(0).getGenericText());
        }

        if (middlewareResponseDTOList.size() == 5 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("generic") && middlewareResponseDTOList.get(2).getType().equals("generic") && middlewareResponseDTOList.get(3).getType().equals("ft_amount_selection")) {
            middlewareResponseDTO.setType("accountBeneficiaryAmountNotMatchedAmountSelection");
            middlewareResponseDTO.setAccountMatchingErrorText(middlewareResponseDTOList.get(0).getGenericText());
            middlewareResponseDTO.setBeneficiaryMatchingErrorText(middlewareResponseDTOList.get(1).getGenericText());
            middlewareResponseDTO.setAmountMatchingErrorText(middlewareResponseDTOList.get(2).getGenericText());
            middlewareResponseDTO.setGenericText(middlewareResponseDTOList.get(3).getGenericText());
        }
        if (middlewareResponseDTOList.size() == 4 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("generic") && middlewareResponseDTOList.get(2).getType().equals("ft_amount_selection")) {
            middlewareResponseDTO.setType("accountBeneficiaryNotMatchedAmountSelection");
            middlewareResponseDTO.setAccountMatchingErrorText(middlewareResponseDTOList.get(0).getGenericText());
            middlewareResponseDTO.setBeneficiaryMatchingErrorText(middlewareResponseDTOList.get(1).getGenericText());
            middlewareResponseDTO.setGenericText(middlewareResponseDTOList.get(2).getGenericText());
        }


        if (middlewareResponseDTOList.size() == 3 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("ft_amount_selection")) {
            middlewareResponseDTO.setType("retryFtAmountSelection");
            middlewareResponseDTO.setRetryText(middlewareResponseDTOList.get(0).getGenericText());
        }
        if (middlewareResponseDTOList.size() == 2 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("ft_amount_selection")) {
            middlewareResponseDTO.setType("retryFtAmountSelection");
            middlewareResponseDTO.setRetryText(middlewareResponseDTOList.get(0).getGenericText());
        }
        if (middlewareResponseDTOList.size() == 4 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(2).getType().equals("ft_info") && middlewareResponseDTOList.get(3).getType().equals("confirmation_button")) {
            middlewareResponseDTO.setType("retryFtConfirmation");
            middlewareResponseDTO.setRetryText(middlewareResponseDTOList.get(0).getGenericText());
            middlewareResponseDTO.setVerifyInfoText(middlewareResponseDTOList.get(1).getGenericText());
            middlewareResponseDTO.setYesButton(middlewareResponseDTOList.get(3).getAmountList().get(0));
            middlewareResponseDTO.setNoButton(middlewareResponseDTOList.get(3).getAmountList().get(1));
        }

        if (middlewareResponseDTOList.size() == 3 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("ft_success_info") && middlewareResponseDTOList.get(2).getType().equals("loop_complete")) {
            middlewareResponseDTO.setType("ftSuccessInfoLoopComplete");
            middlewareResponseDTO.setLoopCompleteText(middlewareResponseDTOList.get(2).getGenericText());
            middlewareResponseDTO.setGenericText(middlewareResponseDTOList.get(0).getGenericText());
        }
        if (middlewareResponseDTOList.size() == 3 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("generic") && middlewareResponseDTOList.get(2).getType().equals("account_selection")) {
            middlewareResponseDTO.setType("accountBeneficiaryNotMatched");
            middlewareResponseDTO.setGenericText(middlewareResponseDTOList.get(2).getGenericText());
            middlewareResponseDTO.setAccountMatchingErrorText(middlewareResponseDTOList.get(0).getGenericText());
            middlewareResponseDTO.setBeneficiaryMatchingErrorText(middlewareResponseDTOList.get(1).getGenericText());
        }

        if (middlewareResponseDTOList.size() == 4 && middlewareResponseDTOList.get(0).getType().equals("generic") && middlewareResponseDTOList.get(1).getType().equals("generic") && middlewareResponseDTOList.get(2).getType().equals("generic") && middlewareResponseDTOList.get(3).getType().equals("account_selection")) {
            middlewareResponseDTO.setType("accountBeneficiaryAmountNotMatched");
            middlewareResponseDTO.setGenericText(middlewareResponseDTOList.get(3).getGenericText());
            middlewareResponseDTO.setAccountMatchingErrorText(middlewareResponseDTOList.get(0).getGenericText());
            middlewareResponseDTO.setBeneficiaryMatchingErrorText(middlewareResponseDTOList.get(1).getGenericText());
            middlewareResponseDTO.setAmountMatchingErrorText(middlewareResponseDTOList.get(2).getGenericText());
        }


        return middlewareResponseDTO;
    }
}