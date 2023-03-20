package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/***
 * TODO: merchant link 추가
 * ku, ku2, gn, sw, hd
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "merchant")
public class Merchant extends AuditingFields {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private Long id;

//    @Setter
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "account_id")
//    private Account account;

    @Column(name = "merchant_name")
    private String name;

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

    @OneToMany(mappedBy = "merchant")
    private List<Theme> themeList = new ArrayList<>();

    @Column(name = "code")
    private String code;
}
