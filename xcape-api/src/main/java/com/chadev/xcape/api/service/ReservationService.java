package com.chadev.xcape.api.service;

import com.chadev.xcape.api.controller.request.AuthenticationRequest;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationAuthenticationDto;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.ReservationAuthentication;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.chadev.xcape.core.service.notification.NotificationTemplateEnum.AUTHENTICATION;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final ReservationAuthenticationRepository reservationAuthenticationRepository;

    private final CoreThemeRepository themeRepository;
    private final CoreMerchantRepository merchantRepository;
    private final CorePriceRepository priceRepository;
    private final DtoConverter dtoConverter;
    private final KakaoTalkNotification kakaoTalkNotification;
    private final SmsNotification smsNotification;
    private final ReservationAuthenticationRepository authenticationRepository;

    // 지점별 빈 예약 만들기(for batch)
    @Transactional
    public void createEmptyReservationByMerchantId(Long merchantId, LocalDate date) throws IllegalArgumentException {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(XcapeException::NOT_EXISTENT_MERCHANT);
        List<Theme> themes = themeRepository.findThemesByMerchant(merchant);
        List<Reservation> reservationList = new ArrayList<>();
        for (Theme theme : themes) {
            String[] timeTableSplit = theme.getTimetable().split(",");
            for (String time : timeTableSplit) {
                List<Integer> timeList = Arrays.stream(time.split(":")).map(Integer::parseInt).toList();
                reservationList.add(new Reservation(merchant, LocalDate.now() + "-" + UUID.randomUUID(), date, LocalTime.of(timeList.get(0), timeList.get(1)), theme.getId(), theme.getNameKo()));
            }
        }
        reservationRepository.saveAll(reservationList);
    }

    // 테마, 날짜로 reservationList 조회
    public List<ReservationDto> getReservationsByThemeIdAndDate(Long themeId, LocalDate date) {
        return reservationRepository.findByThemeIdAndDate(themeId, date).stream().map(dtoConverter::toReservationDto).toList();
    }

    // 예약 등록/수정
    @Transactional
    public ReservationDto registerReservationById(String reservationId, String reservedBy, String phoneNumber, Integer participantCount, String roomType, String requestId, String authenticationNumber) {
        ReservationAuthenticationDto reservationAuthenticationDto = ReservationAuthenticationDto.from(reservationAuthenticationRepository.findById(requestId).orElseThrow(IllegalArgumentException::new));
        if (LocalDateTime.now().isAfter(reservationAuthenticationDto.getRegisteredAt().plusMinutes(3L))) {  //  시간초과
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_TIME_OUT.getCode()), ErrorCode.AUTHENTICATION_TIME_OUT.getMessage());
        } else if (!Objects.equals(reservationAuthenticationDto.getAuthenticationNumber(), authenticationNumber)) { //  인증번호 미일치
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_INVALID_NUMBER.getCode()), ErrorCode.AUTHENTICATION_INVALID_NUMBER.getMessage());
        } else {
            Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);
            boolean isRegister = !reservation.getIsReserved();
            reservation.setIsReserved(true);
            reservation.setReservedBy(reservedBy);
            reservation.setPhoneNumber(phoneNumber);
            // set price
            reservation.setPrice(priceRepository.findByThemeAndPersonAndType(themeRepository.findById(reservation.getThemeId()).orElseThrow(XcapeException::NOT_EXISTENT_THEME), participantCount, roomType).getPrice());
            reservation.setParticipantCount(participantCount);
            reservation.setRoomType(roomType);

            Reservation savedReservation = reservationRepository.save(reservation);
            if (isRegister) reservationHistoryRepository.save(ReservationHistory.register(savedReservation));
            else reservationHistoryRepository.save(ReservationHistory.modify(savedReservation));

            return dtoConverter.toReservationDto(savedReservation);
        }
    }

    // 예약 취소
    @Transactional
    public void cancelReservationById(String reservationId, String requestId, String authenticationNumber) {
        ReservationAuthentication reservationAuthentication = reservationAuthenticationRepository.findById(requestId).orElseThrow(IllegalArgumentException::new);
        ReservationAuthenticationDto reservationAuthenticationDto = ReservationAuthenticationDto.from(reservationAuthentication);
        if (LocalDateTime.now().isAfter(reservationAuthenticationDto.getRegisteredAt().plusMinutes(3L))) {  //  시간초과
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_TIME_OUT.getCode()), ErrorCode.AUTHENTICATION_TIME_OUT.getMessage());
        } else if (!Objects.equals(reservationAuthenticationDto.getAuthenticationNumber(), authenticationNumber)) { //  인증번호 미일치
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_INVALID_NUMBER.getCode()), ErrorCode.AUTHENTICATION_INVALID_NUMBER.getMessage());
        } else {
            Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
            reservationHistoryRepository.save(ReservationHistory.cancel(reservation));
            reservation.setIsReserved(false);
            reservation.setReservedBy(null);
            reservation.setPhoneNumber(null);
            reservation.setPrice(null);
            reservation.setParticipantCount(null);
            reservation.setRoomType(null);
            reservation.setUnreservedTime(null);
            reservationRepository.save(reservation);
        }
    }

    // 가예약 취소
    @Transactional
    public void cancelFakeReservationById(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);
        reservationHistoryRepository.save(ReservationHistory.cancel(reservation));
        reservation.setIsReserved(false);
        reservation.setReservedBy(null);
        reservation.setPhoneNumber(null);
        reservation.setPrice(null);
        reservation.setParticipantCount(null);
        reservation.setRoomType(null);
        reservation.setUnreservedTime(null);
        reservationRepository.save(reservation);
    }

    // 예약 상세 조회
    public ReservationDto getReservation(String reservationId) {
        return new ReservationDto(reservationRepository.findById(reservationId).orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION));
    }

    // 휴대폰 번호로 예약 이력 조회
    public List<ReservationHistoryDto> getReservationHistoryList(String phoneNumber) {
        List<ReservationHistory> histories = reservationHistoryRepository.findReservationHistoriesByPhoneNumber(phoneNumber);
        return histories.stream().map(dtoConverter::toReservationHistoryDto).toList();
    }

    // 현재 시간 가예약 조회
    public List<ReservationDto> getFakeReservationByLocalTime() {
        LocalTime localTime = LocalTime.now();
        log.info("localTime={}", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        return reservationRepository.findReservationsByUnreservedTimeBetweenAndDate(localTime.minusMinutes(1), localTime.plusMinutes(1), LocalDate.now()).stream().map(dtoConverter::toReservationDto).toList();
    }

    /*
    * kakaoTalk 메시지로 인증번호 실패 시 sms로 발송되게끔 구현되어 있습니다.
    * */
    public ReservationAuthenticationDto sendAuthenticationMessage(AuthenticationRequest authenticationRequest) {
        Reservation reservation = reservationRepository.findById(authenticationRequest.getReservationId())
                .orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);

        if (reservation.getIsReserved()) {
            throw XcapeException.ALREADY_RESERVATION();
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
