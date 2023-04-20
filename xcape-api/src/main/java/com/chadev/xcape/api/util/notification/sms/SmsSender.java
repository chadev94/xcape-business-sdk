package com.chadev.xcape.api.util.notification.sms;

import com.chadev.xcape.core.domain.dto.ReservationAuthenticationDto;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.ReservationAuthentication;
import com.chadev.xcape.core.exception.ApiException;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.ReservationAuthenticationRepository;
import com.chadev.xcape.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsSender {

    @Value("${sms.appKey}")
    private String appKey;

    @Value("${sms.secretKey}")
    private String secretKey;

    @Value("${sms.host}")
    private String host;

    private final ReservationRepository reservationRepository;
    private final ReservationAuthenticationRepository authenticationRepository;
    private final RestTemplate restTemplate;

    public SmsResponse sendSms(SmsRequest request) throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("X-Secret-Key", secretKey);
        HttpEntity<?> entity = new HttpEntity<>(request, header);
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(host + "/sms/v3.0/appKeys/" + appKey + "/sender/sms").build();
        ResponseEntity<SmsResponse> response = restTemplate.exchange(uri.toUriString(), HttpMethod.POST, entity, SmsResponse.class);

        if (ObjectUtils.isEmpty(response) | response.getStatusCode() != HttpStatus.OK) {
            throw ApiException.API_EXCEPTION("SMS API response error. response status code: " + response.getStatusCode());
        }

        return response.getBody();
    }

    public ReservationAuthenticationDto sendAuthenticationSms(Long reservationId, String recipientNo) throws Exception{
        ReservationAuthenticationDto reservationAuthenticationDto;

        SmsRequest request = SmsRequest.authenticate(recipientNo);
        String authenticationNumber = request.getRecipientList().get(0).getTemplateParameter().get("authentication");
        // request 설정
        SmsResponse smsResponse = sendSms(request);

        if (!smsResponse.getHeader().isSuccessful) {
            throw new ApiException(smsResponse.getHeader().getResultCode(), smsResponse.getHeader().getResultMessage());
        }

        String requestId = smsResponse
                .getBody()
                .getData()
                .getRequestId();

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);
        ReservationAuthentication reservationAuthentication = new ReservationAuthentication(requestId, reservation, authenticationNumber);
        ReservationAuthentication savedAuthentication = authenticationRepository.save(reservationAuthentication);

        reservationAuthenticationDto = ReservationAuthenticationDto.fromResponseClient(savedAuthentication);

        return reservationAuthenticationDto;
    }

}
