package com.chadev.xcape.core.service.notification.kakao;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class KakaoTalkRequest {
    private final String senderKey;

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
        private String recipientGroupingKey;
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
