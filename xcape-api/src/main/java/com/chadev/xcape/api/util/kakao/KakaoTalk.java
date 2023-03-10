package com.chadev.xcape.api.util.kakao;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Builder
@RequiredArgsConstructor
public class KakaoTalk {

    @Value("${kakao.senderKey}")
    private final String senderKey;

    @Value("${kakao.senderKey}")
    private final String templateCode;

    private List<Recipient> recipientList;

    @Builder
    public static class Button {
        private String ordering;
        private String target;
    }

    @Builder
    public static class Recipient {
        private String recipientNo;
        private Map<String, String> templateParameter;
        private ResendParameter resendParameter;
        private List<Button> buttons;
    }

    @Builder
    public static class ResendParameter {
        private String isResend;
        private String resendType;
        private String resendContent;
        private String resendSendNo;
    }

}
