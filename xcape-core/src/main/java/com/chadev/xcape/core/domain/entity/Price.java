package com.chadev.xcape.core.domain.entity;

import com.chadev.xcape.core.domain.dto.PriceDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "price")
@Getter @NoArgsConstructor
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long id;

    @Setter @Column(name = "person")
    private Integer person;

    @Setter @Column(name = "price")
    private Integer price;

    @Setter @Column(name = "type")
    private String type;

    @Setter @Column(name = "use_yn")
    private Boolean useYn;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public Price(PriceDto priceDto, Merchant merchant, Theme theme) {
        this.person = priceDto.getPerson();
        this.price = priceDto.getPrice();
        this.type = priceDto.getType();
        this.useYn = priceDto.getUseYn();
        this.merchant = merchant;
        this.theme = theme;
    }
}
