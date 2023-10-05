package com.chadev.xcape.admin.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.repository.ReservationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationHistoryService {

    private final ReservationHistoryRepository reservationHistoryRepository;
    private final DtoConverter dtoConverter;

    public List<ReservationHistoryDto> getReservationHistoryListByReservationSeq(long reservationSeq) {
        List<ReservationHistory> reservationHistoryList = reservationHistoryRepository.findReservationHistoriesByReservationSeqOrderByRegisteredAt(reservationSeq);
        return reservationHistoryList.stream().map(dtoConverter::toReservationHistoryDto).toList();
    }
}
