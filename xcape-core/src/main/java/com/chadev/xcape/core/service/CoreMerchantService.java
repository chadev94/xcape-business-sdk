package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.AbilityDto;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.dto.PriceDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CoreMerchantService {

    private final CoreMerchantRepository coreMerchantRepository;
    private final DtoConverter dtoConverter;

    public MerchantDto getMerchantById(Long merchantId) {
        Merchant merchant = coreMerchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toMerchantDtoWithThemeList(merchant);
    }

    public List<MerchantDto> getAllMerchantsWithThemes() {
        return coreMerchantRepository.findAllMerchantsWithThemes().stream().map(dtoConverter::toMerchantDtoWithThemeList).toList();
    }

    @Cacheable(value = "themeInfo", key = "#merchantId")
    public MerchantDto getMerchantWithAllInfo(Long merchantId) {
        return dtoConverter.toMerchantDtoWithThemeList(coreMerchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new));
    }

    public List<Long> getMerchantIdList() {
        return coreMerchantRepository.findAllMerchantsId();
    }

    public List<MerchantDto> getMerchantIdAndNameList() {
        return coreMerchantRepository.findAll().stream()
                .map(entity -> new MerchantDto(entity.getId(), entity.getName())).toList();
    }

}
