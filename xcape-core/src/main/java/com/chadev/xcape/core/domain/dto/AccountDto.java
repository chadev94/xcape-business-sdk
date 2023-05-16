package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Account;
import com.chadev.xcape.core.domain.type.AccountType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

    private Long seq;

    private String id;

    private String userName;

    private String password;

    private AccountType type;

    public AccountDto(Account entity) {
        this.seq = entity.getSeq();
        this.id = entity.getId();
        this.type = entity.getType();
        this.userName = entity.getUserName();
        this.password = entity.getPassword();
    }
}
