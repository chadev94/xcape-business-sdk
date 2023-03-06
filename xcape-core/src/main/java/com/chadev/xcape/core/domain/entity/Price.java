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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Setter @Column(name = "person")
    private Integer person;

    @Setter @Column(name = "price")
    private Integer price;

    @Setter @Column(name = "type")
    private String type;

    @Setter @Column(name = "use_yn")
    private Boolean useYn;

    public Price(PriceDto priceDto, Theme theme) {
        this.theme = theme;
        this.person = priceDto.getPerson();
        this.price = priceDto.getPrice();
        this.type = priceDto.getType();
        this.useYn = priceDto.getUseYn();
    }
}
