package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Account;
import com.chadev.xcape.core.domain.type.AccountRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private AccountRole role;

    private Long merchantId;

    public AccountDto(Account entity) {
        this.id = entity.getId();
        this.role = entity.getRole();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        if (entity.getMerchant() != null) {
            this.merchantId = entity.getMerchant().getId();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> String.valueOf(this.getRole()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
