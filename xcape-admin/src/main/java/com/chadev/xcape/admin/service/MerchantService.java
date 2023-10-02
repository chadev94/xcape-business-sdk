package com.chadev.xcape.admin.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final DtoConverter dtoConverter;

    public List<MerchantDto> getAllMerchantList() {
        return merchantRepository.findAll().stream().map(dtoConverter::toMerchantDto).toList();
    }

    public MerchantDto getMerchant(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toMerchantDto(merchant);
    }

    public List<MerchantDto> getAllMerchantsWithThemes() {
        return merchantRepository.findAllMerchantsWithThemes().stream().map((merchant -> {
            MerchantDto merchantDto = dtoConverter.toMerchantDto(merchant);
            merchantDto.setThemeList(merchant.getThemeList().stream().map(dtoConverter::toThemeDto).toList());
            return merchantDto;
        })).toList();
    }

    public MerchantDto getMerchantWithThemeList(Long merchantId) {
        Merchant merchant = merchantRepository.findMerchantWithThemes(merchantId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toMerchantDtoWithThemeList(merchant);
    }

    public List<MerchantDto> getMerchantIdAndNameList() {
        return merchantRepository.findAll().stream()
                .map(entity -> new MerchantDto(entity.getId(), entity.getName())).toList();
    }
}
