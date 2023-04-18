package com.chadev.xcape.api.util.notification.sms;

import com.chadev.xcape.core.domain.dto.ReservationAuthenticationDto;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.ReservationAuthentication;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.ReservationAuthenticationRepository;
import com.chadev.xcape.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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

    public ReservationAuthenticationDto sendAuthenticationSms(Long reservationId, String recipientNo) {
        SmsRequest request = SmsRequest.authenticate(recipientNo);
        String authenticationNumber = request.getRecipientList().get(0).getTemplateParameter().get("authentication");
        // request 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("X-Secret-Key", secretKey);
        HttpEntity<?> entity = new HttpEntity<>(request, header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(host + "/sms/v3.0/appKeys/" + appKey + "/sender/sms").build();
        ResponseEntity<SmsResponse> response = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, SmsResponse.class);

        String requestId = response.getBody()
                .getBody()
                .getData()
                .getRequestId();

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);
        ReservationAuthentication reservationAuthentication = new ReservationAuthentication(requestId, reservation, authenticationNumber);
        ReservationAuthentication savedAuthentication = authenticationRepository.save(reservationAuthentication);

        return ReservationAuthenticationDto.from(savedAuthentication);
    }

}
