package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.entity.Account;
import com.chadev.xcape.core.domain.entity.Merchant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.CoreThemeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CoreMerchantService {

    private final CoreMerchantRepository coreMerchantRepository;
    private final CoreThemeRepository coreThemeRepository;
    private final DtoConverter dtoConverter;

    @Transactional
    public List<MerchantDto> getMerchantsByAccount(Account account) {
        List<Merchant> merchantByUserId = coreMerchantRepository.findMerchantsByAccount(account);

        return merchantByUserId.stream()
                .map(dtoConverter::toMerchantDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MerchantDto getMerchant(Long merchantId) {
        Optional<Merchant> optional = coreMerchantRepository.findById(merchantId);
        assert optional.isPresent();
        return dtoConverter.toMerchantDto(optional.get());
    }

    //  테스트용
    @Transactional
    public List<MerchantDto> getAllMerchants() {
        List<Merchant> merchantByUserId = coreMerchantRepository.findAll();

        return merchantByUserId.stream()
                .map(dtoConverter::toMerchantDto)
                .collect(Collectors.toList());
    }
}
