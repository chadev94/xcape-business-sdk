package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CoreMerchantService {

    private final CoreMerchantRepository coreMerchantRepository;
    private final DtoConverter dtoConverter;

    public List<MerchantDto> getAllMerchantList() {
        return coreMerchantRepository.findAll().stream().map(dtoConverter::toMerchantDto).toList();
    }

    public MerchantDto getMerchant(Long merchantId) {
        Merchant merchant = coreMerchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toMerchantDto(merchant);
    }

    public List<MerchantDto> getAllMerchantsWithThemes() {
        return coreMerchantRepository.findAllMerchantsWithThemes().stream().map((merchant -> {
            MerchantDto merchantDto = dtoConverter.toMerchantDto(merchant);
            merchantDto.setThemeList(merchant.getThemeList().stream().map(dtoConverter::toThemeDto).toList());
            return merchantDto;
        })).toList();
    }

    public MerchantDto getMerchantWithThemeList(Long merchantId) {
        Merchant merchant = coreMerchantRepository.findMerchantWithThemes(merchantId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toMerchantDtoWithThemeList(merchant);
    }

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
