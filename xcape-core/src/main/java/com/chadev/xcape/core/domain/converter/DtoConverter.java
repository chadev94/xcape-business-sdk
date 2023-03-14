package com.chadev.xcape.core.domain.converter;

import com.chadev.xcape.core.domain.dto.*;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.entity.*;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
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
        return new ReservationDto(entity);
    }

    public PriceDto toPriceDto(Price entity) {
        return new PriceDto(entity);
    }

    public AbilityDto toAbilityDto(Ability entity) {
        return new AbilityDto(entity);
    }

    public ReservationHistoryDto toReservationHistoryDto(ReservationHistory entity) {
        return new ReservationHistoryDto(entity);
    }
}
