package com.chadev.xcape.api.util.notification.kakao;

import com.chadev.xcape.core.domain.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoSender {

    @Value("${kakao.appKey}")
    private String appKey;

    @Value("${kakao.host}")
    private String host;
    private final RestTemplate restTemplate;

    public ResponseEntity<KakaoTalkResponse> sendKakao(ReservationDto reservation) {
        // TODO: 파라미터 정의 후 수정
        Map<String, String> templateParameter = Map.of(
                "reservedBy", reservation.getReservedBy(),
                "merchant", reservation.getMerchantName(),
                "theme", reservation.getThemeName(),
                "time", reservation.getTime().toString(),
                "participantsCount", reservation.getParticipantCount().toString(),
                "price", reservation.getPrice().toString());

        KakaoTalkRequest requestBody = KakaoTalkRequest.builder()
                .recipientList(List.of(KakaoTalkRequest.Recipient.builder()
                        .recipientNo(reservation.getPhoneNumber())
                        .templateParameter(templateParameter)
                        .resendParameter(KakaoTalkRequest.ResendParameter.builder()
                                .isResend(true)
                                .resendType("SMS")
                                .resendContent("sample content")    //  TODO: SMS 내용 정의 후 수정
                                .resendSendNo("01093736081")    //  TODO: 발송 번호 확인 후 수정
                                .build())
                        .buttons(List.of(KakaoTalkRequest.Button.builder()
                                .ordering(0)
                                .target("url")  //  TODO: 예약 상세 / 확인 페이지 url 정의 후 수정
                                .build()))
                        .recipientGroupingKey(reservation.getReservedBy())
                        .build()))
                .build();

        // request 설정
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("X-Secret-Key", ""); //  TODO: 콘솔에서 생성 후 입력
        HttpEntity<?> entity = new HttpEntity<>(requestBody, header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(host + "/alimtalk/v2.2/appkeys/" + appKey + "/messages").build();

        return restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, KakaoTalkResponse.class);
    }
}
