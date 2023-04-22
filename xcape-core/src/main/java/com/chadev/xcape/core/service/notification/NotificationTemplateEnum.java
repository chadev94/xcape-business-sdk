package com.chadev.xcape.core.service.notification;

import com.chadev.xcape.core.service.notification.kakao.KakaoTalkRequest;
import com.chadev.xcape.core.service.notification.sms.SmsRequest;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum NotificationTemplateEnum {

    AUTHENTICATION("인증", "template-authentication", "xcape-authenticate",
            (input) -> {
                AuthenticationParam authenticationParam = (AuthenticationParam) input;
                return Collections.singletonList(KakaoTalkRequest.Recipient.builder()
                        .recipientNo(authenticationParam.getRecipientNo())
                        .templateParameter(Collections.singletonMap("authentication_number", authenticationParam.getAuthenticationNumber()))
                        .resendParameter(KakaoTalkRequest.ResendParameter.builder()
                                .isResend(true)
                                .resendType("SMS")
                                .resendContent("인증번호: " + authenticationParam.getAuthenticationNumber())
                                .resendSendNo("01093736081")    //  TODO: 발송 번호 확인 후 수정
                                .build())
                        .build());
            },
            (input) -> {
                AuthenticationParam authenticationParam = (AuthenticationParam) input;
                return List.of(SmsRequest.Recipient.builder()
                        .recipientNo(authenticationParam.getRecipientNo())
                        .templateParameter(Collections.singletonMap("authentication", authenticationParam.getAuthenticationNumber()))
                        .build());
            }),
//    REGISTER_RESERVATION("예약완료", "template-reservation-register", "xcape-reservation"),
    ;

    private final String description;
    private final String smsTemplateId;
    private final String kakaoTalkTemplateCode;
    private final Function<Object, List<KakaoTalkRequest.Recipient>> kakaoRecipient;
    private final Function<Object, List<SmsRequest.Recipient>> smsRecipient;
    public String senderKey;

    public <T> KakaoTalkRequest getKakaoTalkRequest(T param) {
        return KakaoTalkRequest.builder()
                .senderKey(this.senderKey)
                .templateCode(this.getKakaoTalkTemplateCode())
                .recipientList(this.kakaoRecipient.apply(param))
                .build();
    }

    public <T> SmsRequest getSmsRequest(T param) {
        return SmsRequest.builder()
                .templateId(this.getSmsTemplateId())
                .recipientList(this.smsRecipient.apply(param))
                .build();
    }

    @AllArgsConstructor
    @Getter
    public static class AuthenticationParam {
        private String reservationId;
        private String recipientNo;
        private String authenticationNumber;
    }

    @Component
    @RequiredArgsConstructor
    public static class NotificationTemplateEnumInjector {
        @Value("${kakao.senderKey}")
        protected String senderKey;

        @PostConstruct
        public void postConstruct() {
            NotificationTemplateEnum.AUTHENTICATION.injectSenderKey(senderKey);
//            NotificationTemplateEnum.REGISTER_RESERVATION.injectSenderKey(senderKey);
        }
    }

    private void injectSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }
}
