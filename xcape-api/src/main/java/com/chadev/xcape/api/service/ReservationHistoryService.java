package com.chadev.xcape.api.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.repository.ReservationHistoryRepository;
import com.chadev.xcape.core.response.ReservationHistoryTableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationHistoryService {

    private final ReservationHistoryRepository reservationHistoryRepository;
    private final DtoConverter dtoConverter;

    // 휴대폰 번호로 예약 이력 조회
    public List<ReservationHistoryTableDto> getReservationHistoryList(String phoneNumber) {
        return reservationHistoryRepository.findReservationHistoriesByPhoneNumberOrderByRegisteredAt(phoneNumber).stream().map(dtoConverter::toReservationHistoryTableDto).toList();
    }

    public ReservationHistoryDto getReservationHistory(String reservationHistoryId) {
        ReservationHistory byReservationHistoryId = reservationHistoryRepository.findById(reservationHistoryId);
        return dtoConverter.toReservationHistoryDto(byReservationHistoryId);
    }
}
