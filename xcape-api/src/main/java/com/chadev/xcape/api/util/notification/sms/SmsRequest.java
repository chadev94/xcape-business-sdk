package com.chadev.xcape.api.util.notification.sms;

import com.chadev.xcape.api.util.notification.TemplateType;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SmsRequest {

    @Value("${sms.sendNo}")
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

    public static SmsRequest authenticate(String recipientNo) {
        StringBuilder random = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            random.append((int) (Math.random() * 10));
        }

        Map<String, String> templateParameter = new HashMap<>();
        templateParameter.put("authentication", random.toString());
        return SmsRequest.builder()
                .templateId(TemplateType.AUTHENTICATION.getTemplateId())
                .recipientList(List.of(Recipient.builder()
                        .recipientNo(recipientNo)
                        .templateParameter(templateParameter)
                        .build()))
                .build();
    }

    public static SmsRequest registerReservation(String recipientNo) {

        Map<String, String> templateParameter = new HashMap<>();
//        templateParameter.put("authentication", );
        return SmsRequest.builder()
                .templateId(TemplateType.AUTHENTICATION.getTemplateId())
                .recipientList(List.of(Recipient.builder()
                        .recipientNo(recipientNo)
                        .templateParameter(templateParameter)
                        .build()))
                .build();
    }
}
