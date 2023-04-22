package com.chadev.xcape.api.util.notification.kakao;

import com.chadev.xcape.api.controller.request.AuthenticationRequest;
import com.chadev.xcape.core.domain.dto.ReservationAuthenticationDto;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.ReservationAuthentication;
import com.chadev.xcape.core.exception.ApiException;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.ReservationAuthenticationRepository;
import com.chadev.xcape.core.repository.ReservationRepository;
import com.chadev.xcape.core.util.XcapeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class KakaoSender {

    @Value("${kakao.appKey}")
    private String appKey;
    @Value("${kakao.senderKey}")
    private String senderKey;
    @Value("${kakao.secretKey}")
    private String secretKey;
    @Value("${kakao.templateCode.authenticate}")
    private String authenticateTemplateCode;

    @Value("${kakao.host}")
    private String host;

    private final ReservationRepository reservationRepository;
    private final ReservationAuthenticationRepository authenticationRepository;
    private final RestTemplate restTemplate;


    public KakaoTalkResponse sendKakaoMessage(KakaoTalkRequest kakaoTalkRequest) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("X-Secret-Key", secretKey);
        HttpEntity<?> entity = new HttpEntity<>(kakaoTalkRequest, header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(host + "/alimtalk/v2.2/appkeys/" + appKey + "/messages").build();
        ResponseEntity<KakaoTalkResponse> response = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, KakaoTalkResponse.class);
        KakaoTalkResponse responseBody = Optional.ofNullable(response.getBody())
                .orElseThrow(() -> {throw ApiException.API_EXCEPTION("Kakao API call failed.");});

        if (ObjectUtils.isEmpty(response) |
                response.getStatusCode() != HttpStatus.OK |
                !responseBody.getHeader().isSuccessful()) {
            throw ApiException.API_EXCEPTION("KakaoTalk API response error. Result code: "
                    + responseBody.getHeader().getResultCode()
                    + ", Result message: " + responseBody.getHeader().getResultMessage());
        }

        return responseBody;
    }

    public ReservationAuthenticationDto sendAuthenticationMessage(AuthenticationRequest authenticationRequest) {
        Reservation reservation = reservationRepository.findById(authenticationRequest.getReservationId())
                .orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);

        String authenticationNumber = XcapeUtil.getAuthenticationNumber.get();
        KakaoTalkRequest request = KakaoTalkRequest.builder()
                .senderKey(senderKey)
                .templateCode(authenticateTemplateCode)
                .recipientList(List.of(KakaoTalkRequest.Recipient.builder()
                        .recipientNo(authenticationRequest.getRecipientNo())
                        .templateParameter(Collections.singletonMap("authentication_number", authenticationNumber))
                        .resendParameter(KakaoTalkRequest.ResendParameter.builder()
                                .isResend(true)
                                .resendType("SMS")
                                .resendContent("인증번호: " + authenticationNumber)
                                .resendSendNo("01093736081")    //  TODO: 발송 번호 확인 후 수정
                                .build())
                        .build()))
                .build();
        KakaoTalkResponse kakaoTalkResponse = sendKakaoMessage(request);

        if (!kakaoTalkResponse.getHeader().isSuccessful) {
            throw new ApiException(kakaoTalkResponse.getHeader().getResultCode(), kakaoTalkResponse.getHeader().getResultMessage());
        }

        ReservationAuthentication reservationAuthentication = new ReservationAuthentication(
                kakaoTalkResponse.getMessage().getRequestId(),
                reservation,
                authenticationNumber);

        ReservationAuthentication savedAuthentication = authenticationRepository.save(reservationAuthentication);

        return ReservationAuthenticationDto.fromResponseClient(savedAuthentication);
    }
}
