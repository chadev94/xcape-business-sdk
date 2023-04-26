package com.chadev.xcape.admin.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Price;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final CorePriceRepository priceRepository;
    private final CoreMerchantRepository merchantRepository;

    private final ReservationRepository reservationRepository;
    private final CoreThemeRepository themeRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final DtoConverter dtoConverter;

    public List<ThemeDto> getThemesWithReservations(Long merchantId, LocalDate date){
        return themeRepository.findThemesByMerchantId(merchantId).stream().map((theme) -> {
            ThemeDto themeDto = dtoConverter.toThemeDto(theme);
            themeDto.setReservationList(reservationRepository.findReservationsByThemeIdAndDateOrderBySeq(theme.getId(), date).stream().map(dtoConverter::toReservationDto).toList());
            return themeDto;
        }).toList();
    }

    // 예약 등록/수정
    @Transactional
    public ReservationDto registerReservationById(String reservationId, String reservedBy, String phoneNumber, Integer participantCount, String roomType) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        Theme theme = themeRepository.findById(reservation.getThemeId()).orElseThrow(XcapeException::NOT_EXISTENT_THEME);
        Price price = priceRepository.findFirstByThemeAndPersonAndType(theme, participantCount, roomType);
        boolean isRegister = !reservation.getIsReserved();
        reservation.setIsReserved(true);
        reservation.setReservedBy(reservedBy);
        reservation.setPhoneNumber(phoneNumber);
        // set price
        reservation.setPrice(price.getPrice());
        reservation.setParticipantCount(participantCount);
        reservation.setRoomType(roomType);

        Reservation savedReservation = reservationRepository.save(reservation);
        if (isRegister) reservationHistoryRepository.save(ReservationHistory.register(savedReservation));
        else reservationHistoryRepository.save(ReservationHistory.modify(savedReservation));

        return dtoConverter.toReservationDto(savedReservation);
    }

    // 예약 취소
    @Transactional
    public void cancelReservationById(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        reservationHistoryRepository.save(ReservationHistory.cancel(reservation));
        reservation.setIsReserved(false);
        reservation.setReservedBy(null);
        reservation.setPhoneNumber(null);
        reservation.setPrice(null);
        reservation.setParticipantCount(null);
        reservation.setRoomType(null);
        reservationRepository.save(reservation);
    }

    // 예약 상세 조회
    public ReservationDto getReservation(String reservationId) {
        return new ReservationDto(reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new));
    }

    // 지점별 빈 예약 생성
    public void createEmptyReservationByMerchantId(Long merchantId, LocalDate date) throws IllegalArgumentException {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(XcapeException::NOT_EXISTENT_MERCHANT);
        List<Theme> themes = themeRepository.findThemesByMerchant(merchant);
        for (Theme theme : themes) {
            String[] timeTableSplit = theme.getTimetable().split(",");
            for (String time : timeTableSplit) {
                List<Integer> timeList = Arrays.stream(time.split(":")).map(Integer::parseInt).toList();
                try {
                    reservationRepository.save(new Reservation(merchant, LocalDate.now() + "-" + UUID.randomUUID(), date, LocalTime.of(timeList.get(0), timeList.get(1)), theme.getId(), theme.getNameKo()));
                } catch (Exception e) {
                    log.error(">>> error: ", e);
                }
            }
        }
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
        reservation.setRoomType(fake.getRoomType());
        if (unreservedTime != null) {
            reservation.setUnreservedTime(reservation.getTime().minusMinutes(unreservedTime));
        }
        Reservation savedReservation = reservationRepository.save(reservation);
        return dtoConverter.toReservationDto(savedReservation);
    }

    // 현재 시간 가예약 조회
    public List<ReservationDto> getFakeReservationByLocalTime() {
        LocalTime localTime = LocalTime.now();
        return reservationRepository.findReservationsByUnreservedTimeBetweenAndDate(localTime.minusMinutes(1), localTime.plusMinutes(1), LocalDate.now()).stream().map(dtoConverter::toReservationDto).toList();
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
}
