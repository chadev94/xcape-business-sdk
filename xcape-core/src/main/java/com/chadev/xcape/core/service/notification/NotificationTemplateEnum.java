package com.chadev.xcape.core.service.notification;

import com.chadev.xcape.core.service.notification.kakao.KakaoTalkRequest;
import com.chadev.xcape.core.service.notification.sms.SmsRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
                        .build());
            },
            (input) -> {
                AuthenticationParam authenticationParam = (AuthenticationParam) input;
                return List.of(SmsRequest.Recipient.builder()
                        .recipientNo(authenticationParam.getRecipientNo())
                        .templateParameter(Collections.singletonMap("authentication", authenticationParam.getAuthenticationNumber()))
                        .build());
            }),
    REGISTER_RESERVATION("예약완료", "template-reservation-register", "xcape-reservation",
            (input) -> {
                ReservationSuccessParam reservationSuccessParam = (ReservationSuccessParam) input;
                Map<String, String> map = reservationSuccessParam.getObjectMapper().convertValue(reservationSuccessParam, Map.class);
                return Collections.singletonList(KakaoTalkRequest.Recipient.builder()
                        .recipientNo(reservationSuccessParam.getRecipientNo())
                        .templateParameter(map)
                        .build());
            },
            (input) -> {
                AuthenticationParam authenticationParam = (AuthenticationParam) input;
                return List.of(SmsRequest.Recipient.builder()
                        .recipientNo(authenticationParam.getRecipientNo())
                        .templateParameter(Collections.singletonMap("authentication", authenticationParam.getAuthenticationNumber()))
                        .build());
            }),
    CANCEL_RESERVATION("예약취소", "", "xcape-reserve-cancel",
            (input) -> {
                ReservationCancelParam reservationCancelParam = (ReservationCancelParam) input;
                Map<String, String> map = reservationCancelParam.getObjectMapper().convertValue(reservationCancelParam, Map.class);
                return Collections.singletonList(KakaoTalkRequest.Recipient.builder()
                        .recipientNo(reservationCancelParam.getRecipientNo())
                        .templateParameter(map)
                        .build());
            },
            (input) -> null),
    REMIND_RESERVATION("예약알림", "", "xcape-reserve-remind",
            (input) -> {
                ReservationRemindParam reservationRemindParam = (ReservationRemindParam) input;
                Map<String, String> map = reservationRemindParam.getObjectMapper().convertValue(reservationRemindParam, Map.class);
                return Collections.singletonList(KakaoTalkRequest.Recipient.builder()
                        .recipientNo(reservationRemindParam.getRecipientNo())
                        .templateParameter(map)
                        .build());
            },
            (input) -> null),
    ;

    private final String description;
    private final String smsTemplateId;
    private final String kakaoTalkTemplateCode;
    private final Function<Object, List<KakaoTalkRequest.Recipient>> kakaoRecipient;
    private final Function<Object, List<SmsRequest.Recipient>> smsRecipient;
    public String senderKey;
    public ObjectMapper objectMapper;

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
                .recipientList(this.getSmsRecipient().apply(param))
                .build();
    }

    @AllArgsConstructor
    @Getter
    public static class AuthenticationParam {
        private String reservationId;
        private String recipientNo;
        private String authenticationNumber;
    }

    @AllArgsConstructor
    @Getter
    public static class ReservationSuccessParam {
        private String recipientNo;
        private String date;
        private String time;
        private String merchantName;
        private String themeName;
        private String reservedBy;
        private String phoneNumber;
        private String participantCount;
        private String price;
        @JsonIgnore
        private ObjectMapper objectMapper;
    }

    @AllArgsConstructor
    @Getter
    public static class ReservationCancelParam {
        private String recipientNo;
        private String date;
        private String time;
        private String merchantName;
        private String themeName;
        private String reservedBy;
        private String phoneNumber;
        private String participantCount;
        private String price;
        @JsonIgnore
        private ObjectMapper objectMapper;
    }

    @AllArgsConstructor
    @Getter
    public static class ReservationRemindParam {
        private String recipientNo;
        private String date;
        private String time;
        private String merchantName;
        private String themeName;
        private String reservedBy;
        private String phoneNumber;
        private String participantCount;
        private String price;
        @JsonIgnore
        private ObjectMapper objectMapper;
    }

    @Component
    @RequiredArgsConstructor
    public static class NotificationTemplateEnumInjector {
        @Value("${kakao.senderKey}")
        protected String senderKey;

        @PostConstruct
        public void postConstruct() {
            NotificationTemplateEnum.AUTHENTICATION.injectSenderKey(senderKey);
            NotificationTemplateEnum.REGISTER_RESERVATION.injectSenderKey(senderKey);
            NotificationTemplateEnum.CANCEL_RESERVATION.injectSenderKey(senderKey);
            NotificationTemplateEnum.REMIND_RESERVATION.injectSenderKey(senderKey);
        }
    }

    private void injectSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }
}
