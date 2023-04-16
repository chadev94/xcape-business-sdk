package com.chadev.xcape.api.util.notification.sms;

import com.chadev.xcape.api.util.notification.TemplateType;
import com.chadev.xcape.api.util.notification.kakao.KakaoTalkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class SmsSender {

    @Value("${sms.appKey}")
    private String appKey;

    @Value("${sms.secretKey}")
    private String secretKey;

    @Value("${sms.host}")
    private String host;

    public ResponseEntity<SmsResponse> sendAuthenticationSms(String recipientNo) {
        SmsRequest request = SmsRequest.authenticate(recipientNo);

        // request 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("X-Secret-Key", secretKey);
        HttpEntity<?> entity = new HttpEntity<>(request, header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(host + "/sms/v3.0/appKeys/" + appKey + "/sender/sms").build();
        ResponseEntity<SmsResponse> exchange = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, SmsResponse.class);

        return exchange;
    }
}
