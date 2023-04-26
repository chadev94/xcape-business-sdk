package com.chadev.xcape.admin.service;

import com.chadev.xcape.admin.repository.ThemeRepository;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.repository.CorePriceRepository;
import com.chadev.xcape.core.repository.ReservationHistoryRepository;
import com.chadev.xcape.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final DtoConverter dtoConverter;
    private final CorePriceRepository priceRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;

    public List<ThemeDto> getThemesWithReservations(Long merchantId, LocalDate date){
        return themeRepository.findThemesByMerchantId(merchantId).stream().map((theme) -> {
            ThemeDto themeDto = dtoConverter.toThemeDto(theme);
            themeDto.setReservationList(reservationRepository.findReservationsByThemeIdAndDateOrderById(theme.getId(), date).stream().map(dtoConverter::toReservationDto).toList());
            return themeDto;
        }).toList();
    }

    // 예약 등록/수정
    @Transactional
    public ReservationDto registerReservationById(Long reservationId, String reservedBy, String phoneNumber, Integer participantCount, String roomType) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        boolean isRegister = !reservation.getIsReserved();
        reservation.setIsReserved(true);
        reservation.setReservedBy(reservedBy);
        reservation.setPhoneNumber(phoneNumber);
        // set price
        reservation.setPrice(priceRepository.findFirstByThemeAndPersonAndType(themeRepository.findById(reservation.getThemeId()).orElseThrow(IllegalArgumentException::new), participantCount, roomType).getPrice());
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
    public ReservationDto getReservation(Long reservationId) {
        return new ReservationDto(reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new));
    }


    // 가예약 등록
    public ReservationDto registerFakeReservation(Long reservationId, Long unreservedTime) {
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
}
