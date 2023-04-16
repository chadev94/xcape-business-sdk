package com.chadev.xcape.api.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.ReservationHistoryRepository;
import com.chadev.xcape.core.repository.ReservationRepository;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.CorePriceRepository;
import com.chadev.xcape.core.repository.CoreThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;

    private final CoreThemeRepository themeRepository;
    private final CoreMerchantRepository merchantRepository;
    private final CorePriceRepository priceRepository;

    private final DtoConverter dtoConverter;

    // 지점별 빈 예약 만들기(for batch)
    @Transactional
    public void createEmptyReservationByMerchantId(Long merchantId, LocalDate date) throws IllegalArgumentException {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(XcapeException::NOT_EXISTENT_MERCHANT);
        List<Theme> themes = themeRepository.findThemesByMerchant(merchant);
        for (Theme theme : themes) {
            String[] timeTableSplit = theme.getTimetable().split(",");
            for (String time : timeTableSplit) {
                List<Integer> timeList = Arrays.stream(time.split(":")).map(Integer::parseInt).toList();
                reservationRepository.save(new Reservation(merchant, date, LocalTime.of(timeList.get(0), timeList.get(1)), theme.getId(), theme.getNameKo()));
            }
        }
    }

    // 테마, 날짜로 reservationList 조회
    public List<ReservationDto> getReservationsByThemeIdAndDate(Long themeId, LocalDate date) {
        return reservationRepository.findByThemeIdAndDate(themeId, date).stream().map(dtoConverter::toReservationDto).toList();
    }

    // 예약 등록/수정
    @Transactional
    public ReservationDto registerReservationById(Long reservationId, String reservedBy, String phoneNumber, Integer participantCount, String roomType) {
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

    // 예약 취소
    @Transactional
    public void cancelReservationById(Long reservationId) {
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
    public ReservationDto getReservation(Long reservationId) {
        return new ReservationDto(reservationRepository.findById(reservationId).orElseThrow(XcapeException::NOT_EXISTENT_RESERVATION));
    }

    // 휴대폰 번호로 예약 이력 조회
    public List<ReservationHistoryDto> getReservationHistories(String phoneNumber) {
        return reservationHistoryRepository.findReservationHistoriesByPhoneNumberOrderByDateTime(phoneNumber).stream().map(dtoConverter::toReservationHistoryDto).toList();
    }

    // 현재 시간 가예약 조회
    public List<ReservationDto> getFakeReservationByLocalTime() {
        LocalTime localTime = LocalTime.now();
        log.info("localTime={}", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        return reservationRepository.findReservationsByUnreservedTimeBetweenAndDate(localTime.minusMinutes(1), localTime.plusMinutes(1), LocalDate.now()).stream().map(dtoConverter::toReservationDto).toList();
    }
}
