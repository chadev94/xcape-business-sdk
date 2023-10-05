package com.chadev.xcape.api.service;

import com.chadev.xcape.api.controller.request.AuthenticationRequest;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationAuthenticationDto;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.ReservationAuthentication;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.domain.request.ReservationRequest;
import com.chadev.xcape.core.domain.type.HistoryType;
import com.chadev.xcape.core.domain.type.RoomType;
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

    private final ThemeRepository themeRepository;
    private final PriceRepository priceRepository;
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

            if (reservation.getIsReserved() && reservation.getRoomType().is(RoomType.GENERAL) && !authenticationRequest.isCanceled()) {
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
    public ReservationHistoryDto registerExecute(String reservationId, ReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);

        checkDuplicate(reservation);

        if (reservation.getIsReserved() && RoomType.OPEN_ROOM.is(reservationRequest.getRoomType())) {
            Theme theme = themeRepository.findById(reservation.getThemeId()).orElseThrow(XcapeException::NOT_EXISTENT_THEME);
            int totalParticipantCount = reservation.getParticipantCount() + reservationRequest.getParticipantCount();

            if (totalParticipantCount > theme.getMaxParticipantCount()) {
                throw XcapeException.OVERFLOW_RESERVATION();
            }

            Reservation newReservation = Reservation.builder()
                    .id(reservation.getId())
                    .seq(reservation.getSeq())
                    .merchantName(reservation.getMerchantName())
                    .date(reservation.getDate())
                    .time(reservation.getTime())
                    .themeName(reservation.getThemeName())
                    .reservedBy(reservationRequest.getReservedBy())
                    .phoneNumber(reservationRequest.getPhoneNumber())
                    .participantCount(reservationRequest.getParticipantCount())
                    .price(convertOpenRoomPrice(reservationRequest.getParticipantCount()))
                    .roomType(RoomType.OPEN_ROOM)
                    .build();

            reservation.setIsReserved(true);
            reservation.setParticipantCount(totalParticipantCount);
            reservation.setRoomType(RoomType.OPEN_ROOM);

            reservationRepository.save(reservation);
            ReservationHistory reservationHistory = reservationHistoryRepository.save(ReservationHistory.register(newReservation));

            return dtoConverter.toReservationHistoryDto(reservationHistory);
        } else if (RoomType.GENERAL.is(reservationRequest.getRoomType())) {
            reservation.setIsReserved(true);
            reservation.setReservedBy(reservationRequest.getReservedBy());
            reservation.setPhoneNumber(reservationRequest.getPhoneNumber());
            reservation.setRoomType(RoomType.GENERAL);
            // set price
            Theme theme = themeRepository.findById(reservation.getThemeId()).orElseThrow(XcapeException::NOT_EXISTENT_THEME);
            Integer price = priceRepository.findFirstByThemeAndPerson(theme, reservationRequest.getParticipantCount()).getPrice();
            reservation.setPrice(price);
            reservation.setParticipantCount(reservationRequest.getParticipantCount());

            Reservation savedReservation = reservationRepository.save(reservation);
            ReservationHistory reservationHistory = reservationHistoryRepository.save(ReservationHistory.register(savedReservation));

            return dtoConverter.toReservationHistoryDto(reservationHistory);
        } else {
            Reservation newReservation = Reservation.builder()
                    .id(reservation.getId())
                    .seq(reservation.getSeq())
                    .merchantName(reservation.getMerchantName())
                    .date(reservation.getDate())
                    .time(reservation.getTime())
                    .themeName(reservation.getThemeName())
                    .reservedBy(reservationRequest.getReservedBy())
                    .phoneNumber(reservationRequest.getPhoneNumber())
                    .participantCount(reservationRequest.getParticipantCount())
                    .price(convertOpenRoomPrice(reservationRequest.getParticipantCount()))
                    .roomType(RoomType.OPEN_ROOM)
                    .build();
            // 빈 예약에 처음 오픈룸 예약할 때

            reservation.setIsReserved(true);
            reservation.setParticipantCount(reservationRequest.getParticipantCount());
            reservation.setRoomType(RoomType.OPEN_ROOM);

            reservationRepository.save(reservation);
            ReservationHistory reservationHistory = reservationHistoryRepository.save(ReservationHistory.register(newReservation));

            return dtoConverter.toReservationHistoryDto(reservationHistory);
        }
    }

    private static int convertOpenRoomPrice(Integer participantCount) {
        if (participantCount == 4) {
            return 100000;
        } else if (participantCount == 5) {
            return 115000;
        } else if (participantCount == 6) {
            return 138000;
        }

        return participantCount * 24000;
    }

    private static void checkDuplicate(Reservation reservation) {
        if (reservation.getIsReserved() && RoomType.GENERAL.is(reservation.getRoomType())) {
            throw XcapeException.ALREADY_RESERVATION();
        }
    }

    @Transactional
    @Override
    public ReservationHistoryDto cancelExecute(String reservationHistoryId, ReservationRequest reservationRequest) {
        ReservationHistory reservationHistory = reservationHistoryRepository.findById(reservationHistoryId);

        if (!StringUtils.equals(reservationHistory.getPhoneNumber(), reservationRequest.getRecipientNo())) {
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_INVALID_PHONE_NUMBER.getCode()), ErrorCode.AUTHENTICATION_INVALID_PHONE_NUMBER.getMessage());
        }
        reservationHistory.setType(HistoryType.CANCEL);
        reservationHistoryRepository.save(reservationHistory);

        Reservation reservation = reservationHistory.getReservation();

        if (RoomType.GENERAL.is(reservationRequest.getRoomType())) {
            ReservationHistory deletedReservationHistory = ReservationHistory.cancel(reservation);
            reservation.setIsReserved(false);
            reservation.setReservedBy(null);
            reservation.setPhoneNumber(null);
            reservation.setPrice(null);
            reservation.setParticipantCount(null);
            reservation.setUnreservedTime(null);
            reservationRepository.save(reservation);
            return dtoConverter.toReservationHistoryDto(deletedReservationHistory);
        } else if (RoomType.OPEN_ROOM.is(reservationRequest.getRoomType())) {
            int currentParticipantCount = reservation.getParticipantCount() - reservationHistory.getParticipantCount();

            if (currentParticipantCount == 0) {
                ReservationHistory.cancel(reservation);
                reservation.setIsReserved(false);
                reservation.setReservedBy(null);
                reservation.setPhoneNumber(null);
                reservation.setPrice(null);
                reservation.setParticipantCount(null);
                reservation.setUnreservedTime(null);
                reservation.setRoomType(null);
                reservationRepository.save(reservation);
                return dtoConverter.toReservationHistoryDto(reservationHistory);
            } else {
                ReservationHistory.cancel(reservation);
                reservation.setParticipantCount(currentParticipantCount);
                reservationRepository.save(reservation);
                return dtoConverter.toReservationHistoryDto(reservationHistory);
            }
        }
        throw XcapeException.NOT_EXISTENT_ROOM_TYPE();
    }

    @Override
    public void notify(ReservationHistoryDto reservationHistoryDto, ReservationRequest reservationRequest) {
        KakaoTalkResponse kakaoTalkResponse;

        if (reservationHistoryDto.getType().is(HistoryType.REGISTER)) {
            NotificationTemplateEnum.ReservationSuccessParam reservationSuccessParam = reservationRequest.getReservationSuccessParam(reservationHistoryDto, objectMapper);
            kakaoTalkResponse = kakaoTalkNotification.sendMessage(REGISTER_RESERVATION.getKakaoTalkRequest(reservationSuccessParam));
            if (!kakaoTalkResponse.getHeader().isSuccessful) {
                SmsResponse smsResponse = smsNotification.sendMessage(REGISTER_RESERVATION.getSmsRequest(reservationSuccessParam));
                if (!smsResponse.getHeader().isSuccessful) {
                    throw new ApiException(kakaoTalkResponse.getHeader().getResultCode(), kakaoTalkResponse.getHeader().getResultMessage());
                }
            }
        } else {
            NotificationTemplateEnum.ReservationCancelParam reservationCancelParam = reservationRequest.getReservationCancelParam(reservationHistoryDto, objectMapper);
            kakaoTalkResponse = kakaoTalkNotification.sendMessage(CANCEL_RESERVATION.getKakaoTalkRequest(reservationCancelParam));
        }
    }
}
