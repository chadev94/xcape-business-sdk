package com.chadev.xcape.api.service;

import com.chadev.xcape.api.controller.request.AuthenticationRequest;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationAuthenticationDto;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.ReservationAuthentication;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.domain.request.ReservationRequest;
import com.chadev.xcape.core.domain.type.HistoryType;
import com.chadev.xcape.core.exception.ApiException;
import com.chadev.xcape.core.exception.ErrorCode;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.*;
import com.chadev.xcape.core.service.ReservationServiceInterface;
import com.chadev.xcape.core.service.notification.NotificationTemplateEnum;
import com.chadev.xcape.core.service.notification.kakao.KakaoTalkNotification;
import com.chadev.xcape.core.service.notification.kakao.KakaoTalkResponse;
import com.chadev.xcape.core.service.notification.sms.SmsNotification;
import com.chadev.xcape.core.service.notification.sms.SmsResponse;
import com.chadev.xcape.core.util.XcapeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.chadev.xcape.core.service.notification.NotificationTemplateEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService implements ReservationServiceInterface {
    private final ReservationRepository reservationRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final ReservationAuthenticationRepository reservationAuthenticationRepository;

    private final CoreThemeRepository themeRepository;
    private final CorePriceRepository priceRepository;
    private final DtoConverter dtoConverter;
    private final KakaoTalkNotification kakaoTalkNotification;
    private final SmsNotification smsNotification;
    private final ReservationAuthenticationRepository authenticationRepository;
    private final ObjectMapper objectMapper;

    // 테마, 날짜로 reservationList 조회
    public List<ReservationDto> getReservationsByThemeIdAndDate(Long themeId, LocalDate date) {
        return reservationRepository.findByThemeIdAndDateOrderBySeq(themeId, date)
                .stream()
                .peek(reservation -> {
                    if (LocalDateTime.now()
                            .isAfter(LocalDateTime.of(reservation.getDate(), reservation.getTime()).plusMinutes(10L))
                            && !reservation.getIsReserved()) {
                        reservation.setIsReserved(true);
                    }
                })
                .map(dtoConverter::toReservationDto)
                .toList();
    }

    /*
    * kakaoTalk 메시지로 인증번호 실패 시 sms로 발송되게끔 구현되어 있습니다.
    * */
    public ReservationAuthenticationDto sendAuthenticationMessage(AuthenticationRequest authenticationRequest) {
        Reservation reservation;
        if (authenticationRequest.isCanceled()) { // 예약 취소 요청
            ReservationHistory reservationHistory = reservationHistoryRepository.findById(authenticationRequest.getReservationId());
            reservation = reservationHistory.getReservation(); // 원본 예약
        } else {
            reservation = reservationRepository.findById(authenticationRequest.getReservationId())
                    .orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);

            if (reservation.getIsReserved() && !authenticationRequest.isCanceled()) {
                throw XcapeException.ALREADY_RESERVATION();
            }
        }

        String authenticationNumber = XcapeUtil.getAuthenticationNumber.get();
        NotificationTemplateEnum.AuthenticationParam authenticationParam = authenticationRequest.getAuthenticationParam(authenticationNumber);
        KakaoTalkResponse kakaoTalkResponse = kakaoTalkNotification.sendMessage(AUTHENTICATION.getKakaoTalkRequest(authenticationParam));
        String requestId;

        if (!kakaoTalkResponse.getHeader().isSuccessful) {
            SmsResponse smsResponse = smsNotification.sendMessage(AUTHENTICATION.getSmsRequest(authenticationParam));
            if (!smsResponse.getHeader().isSuccessful) {
                throw new ApiException(kakaoTalkResponse.getHeader().getResultCode(), kakaoTalkResponse.getHeader().getResultMessage());
            }
            requestId = smsResponse.getBody().getData().getRequestId();
        } else {
            requestId = kakaoTalkResponse.getMessage().getRequestId();
        }

        ReservationAuthentication reservationAuthentication = new ReservationAuthentication(
                requestId,
                reservation,
                authenticationNumber);

        ReservationAuthentication savedAuthentication = authenticationRepository.save(reservationAuthentication);

        return ReservationAuthenticationDto.fromResponseClient(savedAuthentication);
    }

    @Override
    public ReservationAuthenticationDto checkTimeOut(ReservationRequest reservationRequest) {
        ReservationAuthentication reservationAuthentication = reservationAuthenticationRepository.findById(reservationRequest.getRequestId()).orElseThrow(IllegalArgumentException::new);
        ReservationAuthenticationDto reservationAuthenticationDto = ReservationAuthenticationDto.from(reservationAuthentication);
        if (LocalDateTime.now().isAfter(reservationAuthenticationDto.getRegisteredAt().plusMinutes(3L))) {
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_TIME_OUT.getCode()), ErrorCode.AUTHENTICATION_TIME_OUT.getMessage());
        }

        return reservationAuthenticationDto;
    }

    @Override
    public void checkAuthenticationCode(ReservationAuthenticationDto reservationAuthenticationDto, String authenticationNumber) {
        if (!Objects.equals(reservationAuthenticationDto.getAuthenticationNumber(), authenticationNumber)) { //  인증번호 미일치
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_INVALID_NUMBER.getCode()), ErrorCode.AUTHENTICATION_INVALID_NUMBER.getMessage());
        }
    }

    @Transactional
    @Override
    public ReservationDto registerExecute(String reservationId, ReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);
        boolean isRegister = !reservation.getIsReserved();
        reservation.setIsReserved(true);
        reservation.setReservedBy(reservationRequest.getReservedBy());
        reservation.setPhoneNumber(reservationRequest.getPhoneNumber());
        // set price
        Theme theme = themeRepository.findById(reservation.getThemeId()).orElseThrow(XcapeException::NOT_EXISTENT_THEME);
        Integer price = priceRepository.findFirstByThemeAndPerson(theme, reservationRequest.getParticipantCount()).getPrice();
        reservation.setPrice(price);
        reservation.setParticipantCount(reservationRequest.getParticipantCount());

        Reservation savedReservation = reservationRepository.save(reservation);
        ReservationHistory reservationHistory;
        if (isRegister) {
            reservationHistory = reservationHistoryRepository.save(ReservationHistory.register(savedReservation));
        } else {
            throw XcapeException.ALREADY_RESERVATION();
        }

        ReservationDto reservationDto = dtoConverter.toReservationDto(savedReservation);
        reservationDto.setReservationHistoryId(reservationHistory.getId());

        return reservationDto;
    }

    @Transactional
    @Override
    public ReservationDto cancelExecute(String reservationHistoryId, ReservationRequest reservationRequest) {
        ReservationHistory reservationHistory = reservationHistoryRepository.findById(reservationHistoryId);

        if (!StringUtils.equals(reservationHistory.getPhoneNumber(), reservationRequest.getRecipientNo())) {
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_INVALID_PHONE_NUMBER.getCode()), ErrorCode.AUTHENTICATION_INVALID_PHONE_NUMBER.getMessage());
        }
        reservationHistory.setType(HistoryType.CANCEL);
        reservationHistoryRepository.save(reservationHistory);

        Reservation reservation = reservationHistory.getReservation();
        Reservation deletedReservation = new Reservation().builder()
                .seq(reservation.getSeq())
                .id(reservation.getId())
                .merchantId(reservation.getMerchantId())
                .merchantName(reservation.getMerchantName())
                .themeId(reservation.getThemeId())
                .themeName(reservation.getThemeName())
                .date(reservation.getDate())
                .time(reservation.getTime())
                .reservedBy(reservation.getReservedBy())
                .phoneNumber(reservation.getPhoneNumber())
                .participantCount(reservation.getParticipantCount())
                .price(reservation.getPrice())
                .isReserved(false)
                .unreservedTime(reservation.getUnreservedTime())
                .build();
        reservation.setIsReserved(false);
        reservation.setReservedBy(null);
        reservation.setPhoneNumber(null);
        reservation.setPrice(null);
        reservation.setParticipantCount(null);
        reservation.setUnreservedTime(null);
        reservationRepository.save(reservation);
        return dtoConverter.toReservationDto(deletedReservation);
    }

    @Override
    public void notify(ReservationDto reservationDto, ReservationRequest reservationRequest) {
        KakaoTalkResponse kakaoTalkResponse;

        if (reservationDto.getIsReserved()) {
            NotificationTemplateEnum.ReservationSuccessParam reservationSuccessParam = reservationRequest.getReservationSuccessParam(reservationDto, objectMapper);
            kakaoTalkResponse = kakaoTalkNotification.sendMessage(REGISTER_RESERVATION.getKakaoTalkRequest(reservationSuccessParam));
            if (!kakaoTalkResponse.getHeader().isSuccessful) {
                SmsResponse smsResponse = smsNotification.sendMessage(REGISTER_RESERVATION.getSmsRequest(reservationSuccessParam));
                if (!smsResponse.getHeader().isSuccessful) {
                    throw new ApiException(kakaoTalkResponse.getHeader().getResultCode(), kakaoTalkResponse.getHeader().getResultMessage());
                }
            }
        } else {
            NotificationTemplateEnum.ReservationCancelParam reservationCancelParam = reservationRequest.getReservationCancelParam(reservationDto, objectMapper);
            kakaoTalkResponse = kakaoTalkNotification.sendMessage(CANCEL_RESERVATION.getKakaoTalkRequest(reservationCancelParam));
        }
    }
}
