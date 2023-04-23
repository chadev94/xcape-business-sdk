package com.chadev.xcape.core.service.notification.sms;

import com.chadev.xcape.core.exception.ApiException;
import com.chadev.xcape.core.service.notification.NotificationInterface;
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
public class SmsNotification implements NotificationInterface<SmsRequest, SmsResponse> {

    @Value("${sms.appKey}")
    private String appKey;

    @Value("${sms.secretKey}")
    private String secretKey;

    @Value("${sms.host}")
    private String host;
    private final RestTemplate restTemplate;

    @Override
    public SmsResponse sendMessage(SmsRequest request) {
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

//    public ReservationAuthenticationDto sendAuthenticationMessage(AuthenticationRequest authenticationRequest) {
//        Reservation reservation = reservationRepository.findById(authenticationRequest.getReservationId())
//                .orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);
//
//        if (reservation.getIsReserved()) {
//            throw XcapeException.ALREADY_RESERVATION();
//        }
//
//        SmsRequest request = SmsRequest.authenticate(authenticationRequest.getRecipientNo());
//        String authenticationNumber = request.getRecipientList().get(0).getTemplateParameter().get("authentication");
//        // request 설정
//        SmsResponse smsResponse = sendMessage(request);
//
//        if (!smsResponse.getHeader().isSuccessful) {
//            throw new ApiException(smsResponse.getHeader().getResultCode(), smsResponse.getHeader().getResultMessage());
//        }
//
//        String requestId = smsResponse
//                .getBody()
//                .getData()
//                .getRequestId();
//
//        ReservationAuthentication reservationAuthentication = new ReservationAuthentication(requestId, reservation, authenticationNumber);
//        ReservationAuthentication savedAuthentication = authenticationRepository.save(reservationAuthentication);
//
//        return ReservationAuthenticationDto.fromResponseClient(savedAuthentication);
//    }

}
