package com.chadev.xcape.admin.service;

import com.chadev.xcape.admin.repository.PriceRepository;
import com.chadev.xcape.admin.repository.ThemeRepository;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.PriceDto;
import com.chadev.xcape.core.domain.entity.Price;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.type.UseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;
    private final ThemeRepository themeRepository;
    private final DtoConverter dtoConverter;

    public List<PriceDto> getPriceListByThemeId(Long themeId) {
        return priceRepository.getPriceListByThemeIdAndUseYn(themeId, UseType.Y.getValue()).stream().map(dtoConverter::toPriceDto).collect(Collectors.toList());
    }

    @Transactional
    public void savePriceList(List<PriceDto> priceDtoList) {
        priceDtoList.forEach(priceDto -> {
            Theme theme = themeRepository.findById(priceDto.getThemeId()).orElseThrow(IllegalArgumentException::new);
            if (priceDto.getId() == null) {
                priceRepository.save(new Price(priceDto, theme));
            } else {
                Price price = priceRepository.findById(priceDto.getId()).orElseThrow(IllegalArgumentException::new);
                price.setPrice(priceDto.getPrice());
                price.setPerson(priceDto.getPerson());
                price.setType(priceDto.getType());
            }
        });
    }
}
