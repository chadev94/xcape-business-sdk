package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CoreMerchantService {

    private final CoreMerchantRepository coreMerchantRepository;
    private final DtoConverter dtoConverter;

    public List<MerchantDto> getAllWithThemes() {
        List<Merchant> findAllMerchants = coreMerchantRepository.findAllWithThemes();
        return findAllMerchants.stream().map(dtoConverter::toMerchantDto).collect(Collectors.toList());
    }

    public MerchantDto getMerchantById(Long merchantId) {
        Merchant merchant = coreMerchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toMerchantDto(merchant);
    }
}
