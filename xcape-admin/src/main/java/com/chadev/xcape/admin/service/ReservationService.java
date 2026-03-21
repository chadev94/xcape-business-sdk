package com.chadev.xcape.admin.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationDetailDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.entity.*;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.domain.request.ReservationRequest;
import com.chadev.xcape.core.domain.type.RoomType;
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
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.chadev.xcape.core.service.notification.NotificationTemplateEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final PriceRepository priceRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final DtoConverter dtoConverter;
    private final TimetableRepository timetableRepository;

    private final KakaoTalkNotification kakaoTalkNotification;
    private final SmsNotification smsNotification;
    private final ObjectMapper objectMapper;

    public List<ThemeDto> getThemesWithReservations(Long merchantId, LocalDate date) {

        List<Reservation> reservationListByMerchantId = reservationRepository.findByMerchantIdAndDateOrderBySeq(merchantId, date);
        List<Theme> themeListByMerchantId = themeRepository.findThemesByMerchantId(merchantId);
        List<ThemeDto> resultThemeList = new ArrayList<>();

        themeListByMerchantId.stream()
                             .filter(Theme::getUseYn)
                             .forEach(theme -> {
                                 List<ReservationDetailDto> reservationListByThemeId =
                                         reservationListByMerchantId.stream()
                                                                    .filter(reservation -> Objects.equals(theme.getId(), reservation.getThemeId()))
                                                                    .map(dtoConverter::toReservationDetailDto).collect(Collectors.toList());
                                 ThemeDto themeDto = dtoConverter.toThemeDto(theme);
                                 themeDto.setReservationDetailList(reservationListByThemeId);
                                 resultThemeList.add(themeDto);
                             });

        return resultThemeList;
    }

    // 예약 등록/수정
    @Transactional
    public ReservationDetailDto registerReservationById(String reservationId, ReservationRequest request) {
        log.info("""
                registerReservationById >>>> request
                reservedBy: {}
                phoneNumber: {}
                participantCount: {}
                """, request.getReservedBy(), request.getPhoneNumber(), request.getParticipantCount());
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        Theme theme = themeRepository.findById(reservation.getThemeId()).orElseThrow(XcapeException::NOT_EXISTENT_THEME);

        if (request.getParticipantCount() == null || request.getParticipantCount() < theme.getMinParticipantCount()) {
            request.setParticipantCount(theme.getMinParticipantCount());
        }

        boolean isRegister = !reservation.getIsReserved();
        if (RoomType.GENERAL.is(request.getRoomType())) {
            Price price = priceRepository.findFirstByThemeAndPerson(theme, request.getParticipantCount());
            reservation.setIsReserved(true);
            reservation.setRoomType(RoomType.GENERAL);
            reservation.setReservedBy(request.getReservedBy());
            reservation.setPhoneNumber(request.getPhoneNumber());
            // set price
            reservation.setPrice(price.getPrice());
            reservation.setParticipantCount(request.getParticipantCount());
        } else if (RoomType.OPEN_ROOM.is(request.getRoomType())) {
            reservation.setIsReserved(true);
            reservation.setRoomType(RoomType.OPEN_ROOM);
            reservation.setPrice(convertOpenRoomPrice(request.getParticipantCount()));
            reservation.setParticipantCount(request.getParticipantCount());
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        ReservationHistory reservationHistory;

        if (isRegister) {
            reservationHistory = reservationHistoryRepository.save(ReservationHistory.register(savedReservation));
        } else {
            reservationHistory = reservationHistoryRepository.save(ReservationHistory.modify(savedReservation));
        }

        reservationHistory.setReservedBy(request.getReservedBy());
        reservationHistory.setPhoneNumber(request.getPhoneNumber());

        ReservationHistoryDto reservationHistoryDto = dtoConverter.toReservationHistoryDto(reservationHistory);
//        reservationHistoryDto.setReservationHistoryId(reservationHistory.getId());
        if (request.getPhoneNumber().startsWith("010")) {
            NotificationTemplateEnum.ReservationSuccessParam reservationSuccessParam = request.getReservationSuccessParam(reservationHistoryDto, objectMapper);

            KakaoTalkResponse kakaoTalkResponse = kakaoTalkNotification.sendMessage(REGISTER_RESERVATION.getKakaoTalkRequest(reservationSuccessParam));
            if (!kakaoTalkResponse.getHeader().isSuccessful) {
                SmsResponse smsResponse = smsNotification.sendMessage(REGISTER_RESERVATION.getSmsRequest(reservationSuccessParam));
                if (!smsResponse.getHeader().isSuccessful) {
                    throw new ApiException(kakaoTalkResponse.getHeader().getResultCode(), kakaoTalkResponse.getHeader().getResultMessage());
                }
            }
        }
        return dtoConverter.toReservationDetailDto(savedReservation);
    }

    // 예약 취소
    @Transactional
    public void cancelReservationById(String reservationId) {
        log.info("cancelReservationById >>>> reservationId: {}", reservationId);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        ReservationDetailDto reservationDetailDto = dtoConverter.toReservationDetailDto(reservation);

        reservationHistoryRepository.save(ReservationHistory.cancel(reservation));
        reservation.setIsReserved(false);
        reservation.setReservedBy(null);
        reservation.setPhoneNumber(null);
        reservation.setPrice(null);
        reservation.setParticipantCount(null);
        reservation.setUnreservedTime(null);
        reservationRepository.save(reservation);

        if (reservation.getRoomType() != null && reservation.getRoomType().is(RoomType.OPEN_ROOM)) {
            reservationDetailDto.setPrice(0);
        }

        if (reservationDetailDto.getPhoneNumber().startsWith("010")) {
            NotificationTemplateEnum.ReservationCancelParam reservationCancelParam = reservationDetailDto.getReservationCancelParam(objectMapper);
            KakaoTalkResponse kakaoTalkResponse = kakaoTalkNotification.sendMessage(CANCEL_RESERVATION.getKakaoTalkRequest(reservationCancelParam));
            if (!kakaoTalkResponse.getHeader().isSuccessful) {
                SmsResponse smsResponse = smsNotification.sendMessage(CANCEL_RESERVATION.getSmsRequest(reservationCancelParam));
                if (!smsResponse.getHeader().isSuccessful) {
                    throw new ApiException(kakaoTalkResponse.getHeader().getResultCode(), kakaoTalkResponse.getHeader().getResultMessage());
                }
            }
        }
    }

    @Transactional
    public void cancelReservationsByIds(List<String> reservationIdList) {
        if (CollectionUtils.isEmpty(reservationIdList)) {
            return;
        }

        reservationIdList.forEach(this::cancelReservationById);
    }

    // 예약 상세 조회
    public ReservationDetailDto getReservation(String reservationId) {
        return new ReservationDetailDto(reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new));
    }

    // 지점별 빈 예약 생성
    public void createEmptyReservationByMerchant(Merchant merchant, LocalDate date) throws IllegalArgumentException {
        List<Theme> themeList = themeRepository.findThemesWithTimeTableListByMerchantId(merchant);
        themeList.forEach(theme ->
                theme.getTimetableList().forEach(timetable -> {
                    try {
                        reservationRepository.save(
                                Reservation.builder()
                                        .id(LocalDate.now() + "-" + UUID.randomUUID())
                                        .merchantId(merchant.getId())
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

    public void reservationBatch(LocalDate date) {
        reservationRepository.reservationBatch(date);
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

    public void reservationReminder() {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalTime targetTime = currentTime.plusMinutes(30).withSecond(0).withNano(0);
        LocalTime startTime = targetTime.minusMinutes(2);
        LocalTime endTime = targetTime.plusMinutes(2);

        List<ReservationDetailDto> reservationDetailDtoList = reservationRepository.findByIsReservedAndDateAndTimeBetweenAndRoomType(true, today, startTime, endTime, RoomType.GENERAL)
                .stream()
                .map(dtoConverter::toReservationDetailDto)
                .toList();

        if (CollectionUtils.isEmpty(reservationDetailDtoList)) {
            return;
        }

        List<NotificationTemplateEnum.ReservationRemindParam> reservationRemindParamList = new ArrayList<>();

        for (ReservationDetailDto reservationDetailDto : reservationDetailDtoList) {
            reservationRemindParamList.add(reservationDetailDto.getReservationRemindParam(objectMapper));
        }

        KakaoTalkResponse kakaoTalkResponse = kakaoTalkNotification.sendMessage(REMIND_RESERVATION.getKakaoTalkRequest(reservationRemindParamList));

        if (!kakaoTalkResponse.getHeader().isSuccessful) {
            SmsResponse smsResponse = smsNotification.sendMessage(REMIND_RESERVATION.getSmsRequest(reservationRemindParamList));
            if (!smsResponse.getHeader().isSuccessful) {
                throw new ApiException(kakaoTalkResponse.getHeader().getResultCode(), kakaoTalkResponse.getHeader().getResultMessage());
            }
        }
    }

    @Transactional
    public void createEmptyReservationByThemeIdAndDate(Long themeId, LocalDate date) {
        List<Timetable> timetableListByThemeId = timetableRepository.findTimetableListByThemeId(themeId);

        Theme theme = themeRepository.findById(themeId).orElseThrow(XcapeException::NOT_EXISTENT_THEME);

        List<Reservation> reservationListByThemeIdAndDate = reservationRepository.findByThemeIdAndDateOrderBySeq(themeId, date);
        if (!reservationListByThemeIdAndDate.isEmpty()) {
            throw XcapeException.ALREADY_RESERVATION();
        }

        List<Reservation> reservationList = new ArrayList<>();

        timetableListByThemeId.forEach(timetable -> {
            String timetableId = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + UUID.randomUUID();
            Reservation newReservation = Reservation.builder()
                    .merchantId(theme.getMerchant().getId())
                    .merchantName(theme.getMerchant().getName())
                    .id(timetableId)
                    .date(date)
                    .time(timetable.getTime())
                    .themeId(themeId)
                    .themeName(theme.getNameKo())
                    .isReserved(false)
                    .build();

            reservationList.add(newReservation);
        });

        reservationRepository.saveAll(reservationList);
    }
}
