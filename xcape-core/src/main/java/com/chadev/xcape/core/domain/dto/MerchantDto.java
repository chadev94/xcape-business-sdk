package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDto {
    private Long id;
    //    private Long accountId;
    private String name;
    private String businessIcon;
    private String address;
    private String telNumber;
    private String businessHour;
    private Boolean parkingYn;
    private String ceoName;
    private String businessRegistrationNumber;
    private String email;
    private String code;
    private Integer order;

    private List<ThemeDto> themeList = new ArrayList<>();

    public MerchantDto(Merchant entity) {
        this.id = entity.getId();
//        this.accountId = accountId;
        this.name = entity.getName();
        this.businessIcon = entity.getBusinessIcon();
        this.address = entity.getAddress();
        this.telNumber = entity.getTelNumber();
        this.businessHour = entity.getBusinessHour();
        this.parkingYn = entity.getParkingYn();
        this.ceoName = entity.getCeoName();
        this.businessRegistrationNumber = entity.getBusinessRegistrationNumber();
        this.email = entity.getEmail();
        this.code = entity.getCode();
        this.order = entity.getOrder();
    }

    public MerchantDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}