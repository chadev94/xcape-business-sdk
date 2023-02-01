package com.chadev.xcape.core.domain.converter;

import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.Theme;
import org.springframework.stereotype.Component;

@Component
public class DtoConverter {

    public MerchantDto toMerchantDto(Merchant merchant) {
        return new MerchantDto(merchant);
    }

    public ThemeDto toThemeDto(Theme theme) {
        return new ThemeDto(theme);
    }

    public ReservationDto toReservationDto(Reservation entity) {
        return new ReservationDto(
                entity.getId(),
                entity.getMerchant().getId(),
                entity.getTheme().getId(),
                entity.getDate(),
                entity.getTime(),
                entity.getReservedBy(),
                entity.getPhoneNumber(),
                entity.getParticipantCount(),
                entity.getPrice(),
                entity.getIsReserved()
        );
    }
}
