package com.chadev.xcape.admin.service;

import com.chadev.xcape.admin.repository.MerchantRepository;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final DtoConverter dtoConverter;

    public List<MerchantDto> getAllMerchants() {
        List<Merchant> findAllMerchants = merchantRepository.findAll();
        return findAllMerchants.stream().map(dtoConverter::toMerchantDto).collect(Collectors.toList());
    }

    public MerchantDto getMerchantById(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toMerchantDto(merchant);
    }
}
