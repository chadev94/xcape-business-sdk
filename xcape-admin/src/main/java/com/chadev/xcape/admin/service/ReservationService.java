package com.chadev.xcape.admin.service;

import com.chadev.xcape.admin.repository.ThemeRepository;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final DtoConverter dtoConverter;

    public List<ThemeDto> getThemesWithReservations(Long merchantId, LocalDate date){
        return themeRepository.findThemesByMerchantId(merchantId).stream().map((theme) -> {
            ThemeDto themeDto = dtoConverter.toThemeDto(theme);
            themeDto.setReservationDtos(reservationRepository.findReservationsByThemeIdAndDateOrderById(theme.getId(), date).stream().map(dtoConverter::toReservationDto).toList());
            return themeDto;
        }).toList();
    }

}
