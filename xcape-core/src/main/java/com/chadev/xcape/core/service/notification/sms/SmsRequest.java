package com.chadev.xcape.core.service.notification.sms;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SmsRequest {
    private final String sendNo;
    private final String body = "";

    private String templateId;
    private List<Recipient> recipientList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Recipient {
        private String recipientNo;
        Map<String, String> templateParameter;
    }

    public SmsRequest(String sendNo, List<Recipient> recipientList) {
        this.sendNo = sendNo;
        this.recipientList = recipientList;
    }
}
