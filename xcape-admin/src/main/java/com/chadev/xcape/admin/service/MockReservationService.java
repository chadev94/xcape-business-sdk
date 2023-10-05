package com.chadev.xcape.admin.service;

import com.amazonaws.util.CollectionUtils;
import com.chadev.xcape.admin.controller.request.RangeMockReservationRequest;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MockReservationService {

    private final ReservationRepository reservationRepository;

    public void registerMockReservations(List<String> reservationIdList, Long unreservedTime) {
        List<Reservation> reservationList = reservationRepository.findByIdIn(reservationIdList);
        List<Reservation> mockReservationList = reservationList.stream()
                                                               .filter(reservation -> !reservation.getIsReserved())
                                                               .peek((reservation -> {
                                                                   ReservationDto mockReservationDto = ReservationDto.fake(reservation);
                                                                   reservation.setIsReserved(mockReservationDto.getIsReserved());
                                                                   reservation.setReservedBy(mockReservationDto.getReservedBy());
                                                                   reservation.setPhoneNumber(mockReservationDto.getPhoneNumber());
                                                                   reservation.setPrice(mockReservationDto.getPrice());
                                                                   reservation.setParticipantCount(mockReservationDto.getParticipantCount());
                                                                   if (unreservedTime != null) {
                                                                       reservation.setUnreservedTime(reservation.getTime().minusMinutes(unreservedTime));
                                                                   }
                                                               })).toList();

        if (CollectionUtils.isNullOrEmpty(mockReservationList)) {
            return;
        }

        reservationRepository.saveAll(mockReservationList);
    }

    public void registerRangeMockReservations(RangeMockReservationRequest rangeMockReservationRequest) {
        LocalDate startDate = rangeMockReservationRequest.getStartDate();
        LocalDate endDate = rangeMockReservationRequest.getEndDate();
        LocalTime startTime = rangeMockReservationRequest.getStartTime();
        LocalTime endTime = rangeMockReservationRequest.getEndTime();

        List<Reservation> reservationList = null;
        if (rangeMockReservationRequest.getMerchantId() != null) {
            reservationList = reservationRepository.findByMerchantIdAndDateBetweenAndTimeBetween(
                    rangeMockReservationRequest.getMerchantId(),
                    startDate,
                    endDate,
                    startTime,
                    endTime
            );
        } else if (rangeMockReservationRequest.getThemeId() != null) {
            reservationList = reservationRepository.findByThemeIdAndDateBetweenAndTimeBetween(
                    rangeMockReservationRequest.getThemeId(),
                    startDate,
                    endDate,
                    startTime,
                    endTime
            );
        }

        if (reservationList != null) {
            List<Reservation> mockReservationList = reservationList.stream()
                    .filter(reservation -> !reservation.getIsReserved())
                    .map(this::toMockReservation)
                    .toList();
            reservationRepository.saveAll(mockReservationList);
        }
    }

    public void cancelUnreservedMockReservations() {
        List<Reservation> unreservedMockReservationsByNowTime = getUnreservedMockReservationsByNowTime();
        List<Reservation> emptyReservationList = unreservedMockReservationsByNowTime.stream().map(this::toEmptyReservation).toList();
        reservationRepository.saveAll(emptyReservationList);
    }

    public List<Reservation> getUnreservedMockReservationsByNowTime() {
        LocalTime time = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);
        LocalDate date = LocalDate.now();
        return reservationRepository.findReservationsByDateAndUnreservedTime(date, time);
    }

    public Reservation toMockReservation(Reservation reservation) {
        reservation.setReservedBy("엑스케이프");
        reservation.setPhoneNumber("0");
        reservation.setParticipantCount(0);
        reservation.setPrice(0);
        reservation.setIsReserved(true);

        return reservation;
    }

    public Reservation toEmptyReservation(Reservation reservation) {
        reservation.setIsReserved(false);
        reservation.setReservedBy(null);
        reservation.setPhoneNumber(null);
        reservation.setPrice(null);
        reservation.setParticipantCount(null);
        reservation.setUnreservedTime(null);

        return reservation;
    }
}
