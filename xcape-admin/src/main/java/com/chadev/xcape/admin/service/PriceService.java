package com.chadev.xcape.admin.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.PriceDto;
import com.chadev.xcape.core.domain.entity.Price;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.PriceRepository;
import com.chadev.xcape.core.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceService {

    private final ThemeRepository themeRepository;
    private final PriceRepository priceRepository;
    private final DtoConverter dtoConverter;

    public List<PriceDto> getPriceListByThemeId(Long themeId) {
        return priceRepository.findPriceListByThemeId(themeId).stream().map(dtoConverter::toPriceDto).toList();
    }

    @Transactional
    public void modifyPriceListByThemeId(List<PriceDto> priceDtoList, Long themeId) {
        Theme theme = themeRepository.findById(themeId).orElseThrow(IllegalArgumentException::new);
        savePriceList(priceDtoList, theme);
    }

    @Transactional
    public void savePriceList(List<PriceDto> priceDtoList, Theme theme) {
        List<Price> priceList = theme.getPriceList();
        priceDtoList.forEach(priceDto -> {
            if (priceDto.getId() == null) {
                priceRepository.save(new Price(priceDto, theme));
            } else if (priceDto.getIsUsed()) {
                Price updatePrice = priceList.stream()
                        .filter(price -> Objects.equals(price.getId(), priceDto.getId()))
                        .findFirst().orElseThrow(XcapeException::NOT_EXISTENT_PRICE);
                updatePrice.setPrice(priceDto.getPrice());
                updatePrice.setPerson(priceDto.getPerson());
            } else {
                Price deletePrice = priceList.stream()
                        .filter(price -> Objects.equals(price.getId(), priceDto.getId()))
                        .findFirst().orElseThrow(XcapeException::NOT_EXISTENT_PRICE);
                priceRepository.delete(deletePrice);
            }
        });
    }
}
