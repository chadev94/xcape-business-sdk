package com.chadev.xcape.api.service;

import com.chadev.xcape.api.controller.request.AuthenticationRequest;
import com.chadev.xcape.api.controller.request.ReservationCancelRequest;
import com.chadev.xcape.api.controller.request.ReservationRegisterRequest;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationAuthenticationDto;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.ReservationAuthentication;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.domain.type.HistoryType;
import com.chadev.xcape.core.exception.ApiException;
import com.chadev.xcape.core.exception.ErrorCode;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.*;
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
public class ReservationService {
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
        return reservationRepository.findByThemeIdAndDateOrderBySeq(themeId, date).stream().map(dtoConverter::toReservationDto).toList();
    }

    // 예약 등록/수정
    @Transactional
    public ReservationDto registerReservationById(String reservationId, ReservationRegisterRequest reservationRegisterRequest) {
        ReservationAuthenticationDto reservationAuthenticationDto = ReservationAuthenticationDto.from(reservationAuthenticationRepository.findById(reservationRegisterRequest.getRequestId()).orElseThrow(IllegalArgumentException::new));
        if (LocalDateTime.now().isAfter(reservationAuthenticationDto.getRegisteredAt().plusMinutes(3L))) {  //  시간초과
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_TIME_OUT.getCode()), ErrorCode.AUTHENTICATION_TIME_OUT.getMessage());
        } else if (!Objects.equals(reservationAuthenticationDto.getAuthenticationNumber(), reservationRegisterRequest.getAuthenticationNumber())) { //  인증번호 미일치
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_INVALID_NUMBER.getCode()), ErrorCode.AUTHENTICATION_INVALID_NUMBER.getMessage());
        } else {
            Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);
            boolean isRegister = !reservation.getIsReserved();
            reservation.setIsReserved(true);
            reservation.setReservedBy(reservationRegisterRequest.getReservedBy());
            reservation.setPhoneNumber(reservationRegisterRequest.getPhoneNumber());
            // set price
            reservation.setPrice(priceRepository.findFirstByThemeAndPerson(themeRepository.findById(reservation.getThemeId()).orElseThrow(XcapeException::NOT_EXISTENT_THEME), reservationRegisterRequest.getParticipantCount()).getPrice());
            reservation.setParticipantCount(reservationRegisterRequest.getParticipantCount());

            Reservation savedReservation = reservationRepository.save(reservation);
            ReservationHistory reservationHistory;
            if (isRegister) {
                reservationHistory = reservationHistoryRepository.save(ReservationHistory.register(savedReservation));
            } else {
                reservationHistory = reservationHistoryRepository.save(ReservationHistory.modify(savedReservation));
            }

            ReservationDto reservationDto = dtoConverter.toReservationDto(savedReservation);
            reservationDto.setReservationHistoryId(reservationHistory.getId());
            NotificationTemplateEnum.ReservationSuccessParam reservationSuccessParam = reservationRegisterRequest.getReservationSuccessParam(reservationDto, objectMapper);
            KakaoTalkResponse kakaoTalkResponse = kakaoTalkNotification.sendMessage(REGISTER_RESERVATION.getKakaoTalkRequest(reservationSuccessParam));

            if (!kakaoTalkResponse.getHeader().isSuccessful) {
                SmsResponse smsResponse = smsNotification.sendMessage(REGISTER_RESERVATION.getSmsRequest(reservationSuccessParam));
                if (!smsResponse.getHeader().isSuccessful) {
                    throw new ApiException(kakaoTalkResponse.getHeader().getResultCode(), kakaoTalkResponse.getHeader().getResultMessage());
                }
            }

            return reservationDto;
        }
    }

    // 예약 취소
    @Transactional
    public void cancelReservationById(String reservationHistoryId, ReservationCancelRequest request) {
        ReservationAuthentication reservationAuthentication = reservationAuthenticationRepository.findById(request.getRequestId()).orElseThrow(IllegalArgumentException::new);
        ReservationAuthenticationDto reservationAuthenticationDto = ReservationAuthenticationDto.from(reservationAuthentication);
        if (LocalDateTime.now().isAfter(reservationAuthenticationDto.getRegisteredAt().plusMinutes(3L))) {  //  시간초과
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_TIME_OUT.getCode()), ErrorCode.AUTHENTICATION_TIME_OUT.getMessage());
        } else if (!Objects.equals(reservationAuthenticationDto.getAuthenticationNumber(), request.getAuthenticationNumber())) { //  인증번호 미일치
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_INVALID_NUMBER.getCode()), ErrorCode.AUTHENTICATION_INVALID_NUMBER.getMessage());
        } else {
            ReservationHistory reservationHistory = reservationHistoryRepository.findById(reservationHistoryId);
            Reservation reservation = reservationHistory.getReservation();

            if (!StringUtils.equals(reservationHistory.getPhoneNumber(), request.getRecipientNo())) {
                throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_INVALID_PHONE_NUMBER.getCode()), ErrorCode.AUTHENTICATION_INVALID_PHONE_NUMBER.getMessage());
            }
            reservationHistory.setType(HistoryType.CANCEL);
            reservationHistoryRepository.save(reservationHistory);

            ReservationDto reservationDto = dtoConverter.toReservationDto(reservation);
            reservationDto.setReservationHistoryId(reservationHistory.getId());
            NotificationTemplateEnum.ReservationCancelParam reservationCancelParam = request.getReservationCancelParam(reservationDto, objectMapper);
            KakaoTalkResponse kakaoTalkResponse = kakaoTalkNotification.sendMessage(CANCEL_RESERVATION.getKakaoTalkRequest(reservationCancelParam));

            if (!kakaoTalkResponse.getHeader().isSuccessful) {
                log.error("ReservationService >>> cancelReservationById {}", kakaoTalkResponse.getMessage().getSendResults());
            }

            reservation.setIsReserved(false);
            reservation.setReservedBy(null);
            reservation.setPhoneNumber(null);
            reservation.setPrice(null);
            reservation.setParticipantCount(null);
            reservation.setUnreservedTime(null);
            reservationRepository.save(reservation);
        }
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
}
