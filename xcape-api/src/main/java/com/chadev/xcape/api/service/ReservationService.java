package com.chadev.xcape.api.service;

import com.chadev.xcape.core.repository.ReservationHistoryRepository;
import com.chadev.xcape.core.repository.ReservationRepository;
import com.chadev.xcape.core.repository.mapping.ReservationInfo;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.CorePriceRepository;
import com.chadev.xcape.core.repository.CoreThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;

    private final CoreThemeRepository themeRepository;
    private final CoreMerchantRepository merchantRepository;
    private final CorePriceRepository priceRepository;

    // 지점별 빈 예약 만들기(for batch)
    @Transactional
    public void createEmptyReservationByMerchantId(Long merchantId, LocalDate date) throws IllegalArgumentException {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        List<Theme> themes = themeRepository.findThemesByMerchant(merchant);
        for (Theme theme : themes) {
            String[] times = theme.getTimetable().split(",");
            for (String time : times) {
                List<Integer> timeSources = Arrays.stream(time.split(":")).map(Integer::parseInt).toList();
                reservationRepository.save(new Reservation(merchant, theme, date, LocalTime.of(timeSources.get(0), timeSources.get(1))));
            }
        }
    }

    public List<ReservationInfo> getReservationsByThemeIdAndDate(Long themeId, LocalDate date) {
        return reservationRepository.findByThemeAndDate(themeRepository.findById(themeId).orElseThrow(IllegalArgumentException::new), date);
    }

    // 예약 등록/수정
    @Transactional
    public void registerReservationById(Long reservationId, String reservedBy, String phoneNumber, Integer participantCount, String roomType) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        boolean isRegister = !reservation.getIsReserved();
        reservation.setIsReserved(true);
        reservation.setReservedBy(reservedBy);
        reservation.setPhoneNumber(phoneNumber);
        // set price
        reservation.setPrice(priceRepository.findByThemeAndPersonAndType(reservation.getTheme(), participantCount, roomType));

        Reservation savedReservation = reservationRepository.save(reservation);
        if (isRegister) reservationHistoryRepository.save(ReservationHistory.register(savedReservation));
        else reservationHistoryRepository.save(ReservationHistory.modify(savedReservation));
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

        reservationRepository.save(reservation);
    }
}
