package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.PriceDto;
import com.chadev.xcape.core.domain.entity.Price;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.type.UseType;
import com.chadev.xcape.core.repository.CorePriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CorePriceService {

    private final CorePriceRepository corePriceRepository;
    private final DtoConverter dtoConverter;

    public List<PriceDto> getPriceListByThemeId(Long themeId) {
        return corePriceRepository.findPricesByThemeIdAndUseYn(themeId, UseType.Y.getValue()).stream().map(dtoConverter::toPriceDto).collect(Collectors.toList());
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
                updatePrice.setType(priceDto.getType());
            }
        });
    }
}
