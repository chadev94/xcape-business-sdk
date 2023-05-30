package com.chadev.xcape.admin.controller.request;

import com.chadev.xcape.core.domain.type.AccountRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRegisterRequest {

    String username;
    String password;
    List<Long> merchantIdList;
    AccountRole role;
}
