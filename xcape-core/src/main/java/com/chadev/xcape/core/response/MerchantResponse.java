package com.chadev.xcape.core.response;

import com.chadev.xcape.core.domain.dto.MerchantDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MerchantResponse {

    private Long id;
//    private Long accountId;
    private String name;
    private String address;

    private List<ThemeResponse> themeResponseList;

    public static MerchantResponse fromDto(MerchantDto dto) {
        return new MerchantResponse(
                dto.getId(),
//                dto.getAccountId(),
                dto.getName(),
                dto.getAddress(),
                dto.getThemeDtoList().stream().map(ThemeResponse::fromDto).toList()
        );
    }
}
