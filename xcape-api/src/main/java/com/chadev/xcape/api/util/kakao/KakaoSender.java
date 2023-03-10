package com.chadev.xcape.api.util.kakao;

import com.chadev.xcape.core.domain.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoSender {

    @Value("{kakao.appKey}")
    private final String appKey;

    public String sendKakao(ReservationDto reservation) {

        // request 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl("/alimtalk/v2.2/appkeys/" + this.appKey + "/messages").build();

        ResponseEntity<?> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, Object.class);

        // TODO: 파라미터 정의 후 수정
        Map<String, String> templateParameter = Map.of(
                "reservedBy", reservation.getReservedBy(),
                "merchant", reservation.getMerchantName(),
                "theme", reservation.getThemeName(),
                "time", reservation.getTime().toString(),
                "participantsCount", reservation.getParticipantCount().toString(),
                "price", reservation.getPrice().toString());

        KakaoTalk.builder()
                .recipientList(List.of(KakaoTalk.Recipient.builder()
                        .recipientNo(reservation.getPhoneNumber())
                        .templateParameter(templateParameter)
                        .resendParameter(KakaoTalk.ResendParameter.builder()
                                .isResend(true)
                                .resendType("SMS")
                                .resendContent("sample content")    //  TODO: SMS 내용 정의 후 수정
                                .resendSendNo("01093736081")    //  TODO: 발송 번호 확인 후 수정
                                .build())
                        .buttons(List.of(KakaoTalk.Button.builder()
                                .ordering(0)
                                .target("url")  //  예약 상세 / 확인 페이지 url 정의 후 수정
                                .build()))
                        .build()))
                .build();


        return null;
    }
}
