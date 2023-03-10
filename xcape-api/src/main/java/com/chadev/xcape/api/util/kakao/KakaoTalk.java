package com.chadev.xcape.api.util.kakao;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class KakaoTalk {

    @Value("${kakao.senderKey}")
    private final String senderKey;

    @Value("${kakao.senderKey}")
    private final String templateCode;

    private List<Recipient> recipientList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Recipient {
        private String recipientNo;
        private Map<String, String> templateParameter = new HashMap<>();
        private ResendParameter resendParameter;
        private List<Button> buttons = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Button {
        private Integer ordering;
        private String target;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResendParameter {
        private Boolean isResend;
        private String resendType;
        private String resendContent;
        private String resendSendNo;
    }

}
