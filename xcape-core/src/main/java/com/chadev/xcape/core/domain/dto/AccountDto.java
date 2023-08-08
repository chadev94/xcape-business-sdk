package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Account;
import com.chadev.xcape.core.domain.type.AccountType;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class AccountDto implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private AccountType type;

    private Long merchantId;

    public AccountDto(Account entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.type = entity.getType();
        if (entity.getMerchant() != null) {
            this.merchantId = entity.getMerchant().getId();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> String.valueOf(this.getType()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
