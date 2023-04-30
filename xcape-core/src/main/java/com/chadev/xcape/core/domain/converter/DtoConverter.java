package com.chadev.xcape.core.domain.converter;

import com.chadev.xcape.core.domain.dto.*;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.entity.*;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.response.ReservationHistoryTableDto;
import org.springframework.stereotype.Component;

@Component
public class DtoConverter {

    public MerchantDto toMerchantDto(Merchant merchant) {
        return new MerchantDto(merchant);
    }

    public MerchantDto toMerchantDtoWithThemeList(Merchant merchant) {
        MerchantDto merchantDto = toMerchantDto(merchant);
        merchantDto.setThemeList(merchant.getThemeList().stream().map(this::themeDtoWithPriceListAndAbilityList).toList());
        return merchantDto;
    }

    public ThemeDto toThemeDto(Theme theme) {
        return new ThemeDto(theme);
    }

    public ThemeDto themeDtoWithPriceListAndAbilityList(Theme theme) {
        ThemeDto themeDto = toThemeDto(theme);
        themeDto.setAbilityList(theme.getAbilityList().stream().map(this::toAbilityDto).toList());
        themeDto.setPriceList(theme.getPriceList().stream().map(this::toPriceDto).toList());
        return themeDto;
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

    public BannerDto toBannerDto(Banner entity) {
        return new BannerDto(entity);
    }

    public ReservationHistoryTableDto toReservationHistoryTableDto(ReservationHistory entity) {
        return new ReservationHistoryTableDto(entity);
    }

    public TimeTableDto toTimetableDto(Timetable entity) {
        return new TimeTableDto(entity);
    }
}
