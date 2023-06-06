package com.chadev.xcape.admin.controller.request;

import com.chadev.xcape.core.domain.type.AccountRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRegisterRequest {

    String username;
    String password;
    Long merchantId;
    AccountRole role;
}
