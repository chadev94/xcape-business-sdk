package com.chadev.xcape.api.service;

import com.chadev.xcape.api.repository.ReservationRepository;
import com.chadev.xcape.api.repository.mapping.ReservationInfo;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.CoreThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CoreThemeRepository themeRepository;
    private final CoreMerchantRepository merchantRepository;
    private final DtoConverter dtoConverter;

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
}
