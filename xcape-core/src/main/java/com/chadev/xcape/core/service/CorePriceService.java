package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.PriceDto;
import com.chadev.xcape.core.domain.entity.Price;
import com.chadev.xcape.core.domain.entity.Theme;
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
        return corePriceRepository.findPriceListByThemeId(themeId).stream().map(dtoConverter::toPriceDto).toList();
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
                corePriceRepository.save(new Price(priceDto, theme));
            } else {
                Price updatePrice = priceList.stream()
                        .filter(price -> Objects.equals(price.getId(), priceDto.getId()))
                        .findFirst().orElseThrow(IllegalArgumentException::new);
                updatePrice.setPrice(priceDto.getPrice());
                updatePrice.setPerson(priceDto.getPerson());
            }
        });
    }

    public void deletePriceList(List<PriceDto> priceDtoList, Long themeId) {
        List<Price> priceList = corePriceRepository.findPriceListByThemeId(themeId);

        priceDtoList.forEach(priceDto -> {
            Price deletePrice = priceList.stream().filter(
                    price -> Objects.equals(price.getId(), priceDto.getId())
            ).findFirst().orElseThrow(IllegalArgumentException::new);
            corePriceRepository.delete(deletePrice);
        });
    }
}
