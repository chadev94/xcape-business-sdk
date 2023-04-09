package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.PriceDto;
import com.chadev.xcape.core.domain.entity.Price;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.type.UseType;
import com.chadev.xcape.core.repository.CorePriceRepository;
import com.chadev.xcape.core.repository.CoreThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CorePriceService {

    private final CoreThemeRepository coreThemeRepository;
    private final CorePriceRepository corePriceRepository;
    private final DtoConverter dtoConverter;

    public List<PriceDto> getPriceListByThemeId(Long themeId) {
        return corePriceRepository.findPricesByThemeIdAndUseYn(themeId, UseType.Y.getValue()).stream().map(dtoConverter::toPriceDto).toList();
    }

    public List<PriceDto> getPriceListByMerchantId(Long merchantId) {
        return corePriceRepository.findPriceListByMerchantIdAndUseYn(merchantId, UseType.Y.getValue()).stream().map(dtoConverter::toPriceDto).toList();
    }

    @Transactional
    public void modifyPriceListByThemeId(List<PriceDto> priceDtoList, Long themeId) {
        Theme theme = coreThemeRepository.findById(themeId).orElseThrow(IllegalArgumentException::new);
        savePriceList(priceDtoList, theme);
    }

    @Transactional
    public void savePriceList(List<PriceDto> priceDtoList, Theme theme) {
        List<Price> priceList = theme.getPriceList();
        priceDtoList.forEach(priceDto -> {
            if (priceDto.getId() == null) {
                corePriceRepository.save(new Price(priceDto, theme.getMerchant(), theme));
            } else {
                Price updatePrice = priceList.stream()
                        .filter(price -> Objects.equals(price.getId(), priceDto.getId()))
                        .findFirst().orElseThrow(IllegalArgumentException::new);
                updatePrice.setPrice(priceDto.getPrice());
                updatePrice.setPerson(priceDto.getPerson());
                updatePrice.setType(priceDto.getType());
                updatePrice.setUseYn(priceDto.getUseYn());
            }
        });
    }
}
