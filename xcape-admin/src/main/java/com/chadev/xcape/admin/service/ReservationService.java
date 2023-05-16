package com.chadev.xcape.admin.service;

import com.chadev.xcape.admin.controller.request.MockReservationRequest;
import com.chadev.xcape.admin.controller.request.ReservationRegisterRequest;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Price;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.exception.ApiException;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.*;
import com.chadev.xcape.core.service.notification.NotificationTemplateEnum;
import com.chadev.xcape.core.service.notification.kakao.KakaoTalkNotification;
import com.chadev.xcape.core.service.notification.kakao.KakaoTalkResponse;
import com.chadev.xcape.core.service.notification.sms.SmsNotification;
import com.chadev.xcape.core.service.notification.sms.SmsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.chadev.xcape.core.service.notification.NotificationTemplateEnum.CANCEL_RESERVATION;
import static com.chadev.xcape.core.service.notification.NotificationTemplateEnum.REGISTER_RESERVATION;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final CorePriceRepository corePriceRepository;
    private final CoreMerchantRepository coreMerchantRepository;
    private final ReservationRepository reservationRepository;
    private final CoreThemeRepository coreThemeRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final DtoConverter dtoConverter;

    private final KakaoTalkNotification kakaoTalkNotification;
    private final SmsNotification smsNotification;
    private final ObjectMapper objectMapper;

    public List<ThemeDto> getThemesWithReservations(Long merchantId, LocalDate date){
        return coreThemeRepository.findThemesByMerchantId(merchantId).stream().map((theme) -> {
            ThemeDto themeDto = dtoConverter.toThemeDto(theme);
            themeDto.setReservationList(reservationRepository.findReservationsByThemeIdAndDateOrderBySeq(theme.getId(), date).stream().map(dtoConverter::toReservationDto).toList());
            return themeDto;
        }).toList();
    }

    // 예약 등록/수정
    @Transactional
    public ReservationDto registerReservationById(String reservationId, ReservationRegisterRequest request) {
        log.info("""
                registerReservationById >>>> request
                reservedBy: {}
                phoneNumber: {}
                participantCount: {}
                """, request.getReservedBy(), request.getPhoneNumber(), request.getParticipantCount());
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        Theme theme = coreThemeRepository.findById(reservation.getThemeId()).orElseThrow(XcapeException::NOT_EXISTENT_THEME);
        Price price = corePriceRepository.findFirstByThemeAndPerson(theme, request.getParticipantCount());
        boolean isRegister = !reservation.getIsReserved();
        reservation.setIsReserved(true);
        reservation.setReservedBy(request.getReservedBy());
        reservation.setPhoneNumber(request.getPhoneNumber());
        // set price
        reservation.setPrice(price.getPrice());
        reservation.setParticipantCount(request.getParticipantCount());

        Reservation savedReservation = reservationRepository.save(reservation);

        ReservationHistory reservationHistory;
        if (isRegister) {
            reservationHistory = reservationHistoryRepository.save(ReservationHistory.register(savedReservation));
        } else {
            reservationHistory = reservationHistoryRepository.save(ReservationHistory.modify(savedReservation));
        }

        ReservationDto reservationDto = dtoConverter.toReservationDto(savedReservation);
        reservationDto.setReservationHistoryId(reservationHistory.getId());

        NotificationTemplateEnum.ReservationSuccessParam reservationSuccessParam = reservationDto.getReservationSuccessParam(objectMapper);

        KakaoTalkResponse kakaoTalkResponse = kakaoTalkNotification.sendMessage(REGISTER_RESERVATION.getKakaoTalkRequest(reservationSuccessParam));
        if (!kakaoTalkResponse.getHeader().isSuccessful) {
            SmsResponse smsResponse = smsNotification.sendMessage(REGISTER_RESERVATION.getSmsRequest(reservationSuccessParam));
            if (!smsResponse.getHeader().isSuccessful) {
                throw new ApiException(kakaoTalkResponse.getHeader().getResultCode(), kakaoTalkResponse.getHeader().getResultMessage());
            }
        }

        return dtoConverter.toReservationDto(savedReservation);
    }

    // 예약 취소
    @Transactional
    public void cancelReservationById(String reservationId) {
        log.info("cancelReservationById >>>> reservationId: {}", reservationId);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        ReservationDto reservationDto = dtoConverter.toReservationDto(reservation);

        reservationHistoryRepository.save(ReservationHistory.cancel(reservation));
        reservation.setIsReserved(false);
        reservation.setReservedBy(null);
        reservation.setPhoneNumber(null);
        reservation.setPrice(null);
        reservation.setParticipantCount(null);
        reservation.setUnreservedTime(null);
        reservationRepository.save(reservation);

        NotificationTemplateEnum.ReservationCancelParam reservationCancelParam = reservationDto.getReservationCancelParam(objectMapper);
        KakaoTalkResponse kakaoTalkResponse = kakaoTalkNotification.sendMessage(CANCEL_RESERVATION.getKakaoTalkRequest(reservationCancelParam));
        if (!kakaoTalkResponse.getHeader().isSuccessful) {
            SmsResponse smsResponse = smsNotification.sendMessage(CANCEL_RESERVATION.getSmsRequest(reservationCancelParam));
            if (!smsResponse.getHeader().isSuccessful) {
                throw new ApiException(kakaoTalkResponse.getHeader().getResultCode(), kakaoTalkResponse.getHeader().getResultMessage());
            }
        }
    }

    // 예약 상세 조회
    public ReservationDto getReservation(String reservationId) {
        return new ReservationDto(reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new));
    }

    // 지점별 빈 예약 생성
    public void createEmptyReservationByMerchant(Merchant merchant, LocalDate date) throws IllegalArgumentException {
        List<Theme> themeList = coreThemeRepository.findThemesWithTimeTableListByMerchantId(merchant);
        themeList.forEach(theme ->
                theme.getTimetableList().forEach(timetable -> {
                    try {
                        reservationRepository.save(
                                Reservation.builder()
                                        .id(LocalDate.now() + "-" + UUID.randomUUID())
                                        .merchantName(merchant.getName())
                                        .date(date)
                                        .time(timetable.getTime())
                                        .themeId(theme.getId())
                                        .themeName(theme.getNameKo())
                                        .isReserved(false)
                                        .build()
                        );
                    } catch (Exception e) {
                        log.error("ReservationService >>> createEmptyReservationByMerchant >", e);
                    }
                })
        );
    }

    // 가예약 등록
    public ReservationDto registerFakeReservation(String reservationId, Long unreservedTime) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        ReservationDto fake = ReservationDto.fake(reservation);
        reservation.setIsReserved(fake.getIsReserved());
        reservation.setReservedBy(fake.getReservedBy());
        reservation.setPhoneNumber(fake.getPhoneNumber());
        // set price
        reservation.setPrice(fake.getPrice());
        reservation.setParticipantCount(fake.getParticipantCount());
        if (unreservedTime != null) {
            reservation.setUnreservedTime(reservation.getTime().minusMinutes(unreservedTime));
        }
        Reservation savedReservation = reservationRepository.save(reservation);
        return dtoConverter.toReservationDto(savedReservation);
    }

    // 현재 시간 가예약 조회
    public List<ReservationDto> getFakeReservationByLocalTime() {
        LocalTime time = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);
        LocalDate date = LocalDate.now();
        List<Reservation> reservationList = reservationRepository.findReservationsByDateAndUnreservedTime(date, time);
        log.info("getFakeReservationByLocalTime");
        reservationList.forEach((reservation -> log.info("reservation unreservedTime : {} themeName: {} time: {}", reservation.getUnreservedTime(), reservation.getThemeName(), reservation.getTime())));
        return reservationList.stream().map(dtoConverter::toReservationDto).toList();
    }

    // 가예약 취소
    @Transactional
    public void cancelFakeReservationById(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION);
        log.info("cancelFakeReservationById");
        log.info("reservation unreservedTime : {} themeName: {} time: {}", reservation.getUnreservedTime(), reservation.getThemeName(), reservation.getTime());
        reservation.setIsReserved(false);
        reservation.setReservedBy(null);
        reservation.setPhoneNumber(null);
        reservation.setPrice(null);
        reservation.setParticipantCount(null);
        reservation.setUnreservedTime(null);
        reservationRepository.save(reservation);
    }

    public void reservationBatch(LocalDate date) {
        coreMerchantRepository.findAll().forEach((merchant) ->
            createEmptyReservationByMerchant(merchant, date)
        );
    }

    @Transactional
    public void createMockReservations(MockReservationRequest mockReservationRequest) {
    }
}