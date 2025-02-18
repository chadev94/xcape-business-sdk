package com.chadev.xcape.core.domain.entity;

import com.chadev.xcape.core.domain.dto.MerchantDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO: merchant link 추가
 * ku, ku2, gn, sw, hd
 */
@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "merchant")
public class Merchant extends AuditingFields {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private Long id;

    @OneToMany(mappedBy = "merchant")
    private List<Theme> themeList = new ArrayList<>();

//    @Setter
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "account_id")
//    private Account account;

    @Column(name = "merchant_name")
    private String name;

    @Column(name = "merchant_name_for_kakao")
    private String nameForKakao;

    @Column(name = "address")
    private String address;

    @Column(name = "tel_number")
    private String telNumber;

    @Column(name = "business_hour")
    private String businessHour;

    @Column(name = "parking_yn")
    private Boolean parkingYn;

    @Column(name = "ceo_name")
    private String ceoName;

    @Column(name = "business_registration_number")
    private String businessRegistrationNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "merchant_code")
    private String code;

    @Column(name = "merchant_order")
    private Integer order;

    @Column(name = "brand_info_notion_id")
    private String brandInfoNotionId;

    @Column(name = "using_info_notion_id")
    private String usingInfoNotionId;

    @Column(name = "address_info_notion_id")
    private String addressInfoNotionId;

    @OneToMany(mappedBy = "merchant")
    private List<Banner> bannerList = new ArrayList<>();

    @Column(name = "use_yn", length = 1)
    private Boolean useYn;

    @Column(name= "display_name")
    private String displayName;

    public void update(MerchantDto requestDto) {
        this.name = requestDto.getName();
        this.nameForKakao = requestDto.getNameForKakao();
        this.address = requestDto.getAddress();
        this.telNumber = requestDto.getTelNumber();
        this.businessHour = requestDto.getBusinessHour();
        this.parkingYn = requestDto.getParkingYn();
        this.ceoName = requestDto.getCeoName();
        this.businessRegistrationNumber = requestDto.getBusinessRegistrationNumber();
        this.email = requestDto.getEmail();
        this.code = requestDto.getCode();
        this.brandInfoNotionId = requestDto.getBrandInfoNotionId();
        this.usingInfoNotionId = requestDto.getUsingInfoNotionId();
        this.addressInfoNotionId = requestDto.getAddressInfoNotionId();
        this.useYn = requestDto.getUseYn();
        this.displayName = requestDto.getDisplayName().toUpperCase();
    }
}
