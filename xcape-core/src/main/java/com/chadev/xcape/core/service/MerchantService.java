package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Account;
import com.chadev.xcape.core.domain.entity.Merchant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chadev.xcape.core.repository.MerchantRepository;
import com.chadev.xcape.core.repository.ThemeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public List<MerchantDto> getMerchantsByAccount(Account account) {
        List<Merchant> merchantByUserId = merchantRepository.findMerchantsByAccount(account);

        return merchantByUserId.stream()
                .map(this::merchantToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MerchantDto getMerchant(Long merchantId) {
        Optional<Merchant> optional = merchantRepository.findById(merchantId);
        assert optional.isPresent();

        return merchantToDto(optional.get());
    }

    //  테스트용
    @Transactional
    public List<MerchantDto> getAllMerchants() {
        List<Merchant> merchantByUserId = merchantRepository.findAll();

        return merchantByUserId.stream()
                .map(this::merchantToDto)
                .collect(Collectors.toList());
    }

    private MerchantDto merchantToDto(Merchant merchant) {
        List<ThemeDto> collect = themeRepository.findThemesByMerchant(merchant).stream().map(ThemeDto::fromEntity).toList();
        return new MerchantDto(merchant.getId(), merchant.getAccount().getId(), merchant.getName(), merchant.getAddress(), collect);
    }
}
