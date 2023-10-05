package com.chadev.xcape.core.service.notification.kakao;

import com.chadev.xcape.core.exception.ApiException;
import com.chadev.xcape.core.service.notification.NotificationInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class KakaoTalkNotification implements NotificationInterface<KakaoTalkRequest, KakaoTalkResponse> {
    @Value("${kakao.appKey}")
    private String appKey;
    @Value("${kakao.secretKey}")
    private String secretKey;

    @Value("${kakao.host}")
    private String host;
    private final RestTemplate restTemplate;

    @Override
    public KakaoTalkResponse sendMessage(KakaoTalkRequest request) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("X-Secret-Key", secretKey);
        HttpEntity<?> entity = new HttpEntity<>(request, header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(host + "/alimtalk/v2.2/appkeys/" + appKey + "/messages").build();
        ResponseEntity<KakaoTalkResponse> response = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, KakaoTalkResponse.class);
        KakaoTalkResponse responseBody = Optional.ofNullable(response.getBody())
                .orElseThrow(() -> {throw ApiException.API_EXCEPTION("Kakao API call failed.");});

        if (ObjectUtils.isEmpty(response) |
                response.getStatusCode() != HttpStatus.OK) {
            throw ApiException.API_EXCEPTION("KakaoTalk API response error. Result code: "
                    + responseBody.getHeader().getResultCode()
                    + ", Result message: " + responseBody.getHeader().getResultMessage());
        }

        return responseBody;
    }
}
