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
    private final CoreThemeService coreThemeService;
    private final CorePriceService corePriceService;
    private final CoreAbilityService coreAbilityService;
    private final DtoConverter dtoConverter;

    public MerchantDto getMerchantById(Long merchantId) {
        Merchant merchant = coreMerchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toMerchantDto(merchant);
    }

    public List<MerchantDto> getAllMerchantsWithThemes() {
        return coreMerchantRepository.findAllMerchantsWithThemes().stream().map(dtoConverter::toMerchantDto).toList();
    }

    @Cacheable(value = "themeInfo", key = "#merchantId")
    public MerchantDto getMerchantWithAllInfo(Long merchantId) {
        MerchantDto merchant = dtoConverter.toMerchantDto(coreMerchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new));
        List<PriceDto> priceListByMerchantId = corePriceService.getPriceListByMerchantId(merchantId);
        List<AbilityDto> abilityListByMerchantId = coreAbilityService.getAbilityListByMerchantId(merchantId);
        List<ThemeDto> themeList = coreThemeService.getThemesByMerchantId(merchantId);
        themeList.forEach((theme) -> {
            theme.setPriceList(priceListByMerchantId.stream()
                    .filter(price -> Objects.equals(price.getThemeId(), theme.getId())).toList());
            theme.setAbilityList(abilityListByMerchantId.stream()
                    .filter(ability -> Objects.equals(ability.getThemeId(), theme.getId())).toList());
        });
        merchant.setThemeList(themeList);

        return merchant;
    }
}
