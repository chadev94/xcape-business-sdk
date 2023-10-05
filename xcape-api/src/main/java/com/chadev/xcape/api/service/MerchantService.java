package com.chadev.xcape.api.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final DtoConverter dtoConverter;

    public List<MerchantDto> getAllMerchantsWithThemes() {
        return merchantRepository.findAllMerchantsWithThemes().stream().map((merchant -> {
            MerchantDto merchantDto = dtoConverter.toMerchantDto(merchant);
            merchantDto.setThemeList(merchant.getThemeList().stream().map(dtoConverter::toThemeDto).toList());
            return merchantDto;
        })).toList();
    }

    public MerchantDto getMerchantWithAllInfo(Long merchantId) {
        return dtoConverter.toMerchantDtoWithThemeList(merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new));
    }
}
