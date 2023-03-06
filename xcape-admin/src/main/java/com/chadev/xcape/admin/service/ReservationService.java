package com.chadev.xcape.admin.service;

import com.chadev.xcape.admin.controller.response.ReservationResponse;
import com.chadev.xcape.admin.repository.MerchantRepository;
import com.chadev.xcape.admin.repository.ThemeRepository;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.repository.ReservationHistoryRepository;
import com.chadev.xcape.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final MerchantRepository merchantRepository;
    private final ThemeRepository themeRepository;
    private final DtoConverter dtoConverter;

    public List<ReservationResponse> findReservedReservation(Long merchantId, LocalDate date) {
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        List<Reservation> reservations = reservationRepository.findReservationsByIsReservedAndMerchant(true, merchant);
        for (Reservation reservation : reservations) {
            reservationResponses.add(new ReservationResponse(
                    reservation.getId(),
                    reservation.getTheme().getNameKo(),
                    reservation.getReservedBy(),
                    reservation.getPhoneNumber(),
                    reservation.getPrice().getPerson(),
                    LocalDateTime.of(reservation.getDate(), reservation.getTime()),
                    reservationHistoryRepository.findFirstByReservationOrderByIdDesc(reservation).getRegisteredAt(),
                    reservation.getPrice().getPrice()
            ));
        }
        return reservationResponses;
    }

    public List<ThemeDto> getThemesWithReservations(Long merchantId, LocalDate date){
        return themeRepository.findThemesByMerchantId(merchantId).stream().map((theme) -> {
            ThemeDto themeDto = dtoConverter.toThemeDto(theme);
            themeDto.setReservationDtos(reservationRepository.findReservationsByThemeAndDateOrderById(theme, date).stream().map(dtoConverter::toReservationDto).toList());
            return themeDto;
        }).toList();
    }

}
