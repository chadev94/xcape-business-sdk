package com.chadev.xcape.admin.service;

import com.chadev.xcape.admin.controller.request.AccountRegisterRequest;
import com.chadev.xcape.admin.repository.AccountRepository;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.AccountDto;
import com.chadev.xcape.core.domain.entity.Account;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final CoreMerchantRepository coreMerchantRepository;

    private final DtoConverter dtoConverter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("관리자 정보를 찾을 수 없습니다."));
        return dtoConverter.toAccountDto(account);
    }

    public AccountDto createAccount(AccountRegisterRequest request) {
        Merchant merchant = coreMerchantRepository.findById(request.getMerchantId()).orElseThrow(IllegalArgumentException::new);

        Account account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .merchant(merchant)
                .build();

        Account savedAccount = accountRepository.save(account);
        return dtoConverter.toAccountDto(savedAccount);
    }
}
