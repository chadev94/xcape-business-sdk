package com.chadev.xcape.api.service;

import com.chadev.xcape.api.repository.ReservationRepository;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.CoreThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CoreThemeRepository themeRepository;
    private final CoreMerchantRepository merchantRepository;
    private final DtoConverter dtoConverter;

    public List<ReservationDto> getReservationsByThemeAndDate(Long themeId, LocalDate date) {
        Theme theme = themeRepository.findById(themeId).orElseThrow();
        return reservationRepository.findReservationsByStartTimeBetweenAndTheme(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX), theme)
                .stream().map(dtoConverter::toReservationDto).toList();
    }

    public List<ReservationDto> getReservationsByMerchantAndDate(Long merchantId, LocalDate date) {
        LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
        List<ReservationDto> reservationDtos = new ArrayList<>();
        List<Theme> themesByMerchant = themeRepository.findThemesByMerchant(merchantRepository.findById(merchantId).orElseThrow(() ->
        {
            throw new XcapeException.NotExistentMerchantException();
        }));
        for (Theme theme : themesByMerchant) {
            reservationDtos.addAll(
                    reservationRepository.findReservationsByStartTimeBetweenAndTheme(start, end, theme)
                            .stream().map(dtoConverter::toReservationDto).toList()
            );
        }
        return reservationDtos;
    }
}
