package com.chadev.xcape.core.domain.entity;

import com.chadev.xcape.core.domain.dto.PriceDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Entity @Table(name = "price")
@Getter @NoArgsConstructor
public class Price extends AuditingFields {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long id;

    @Column(name = "person")
    private Integer person;

    @Column(name = "price")
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public Price(PriceDto priceDto, Theme theme) {
        this.person = priceDto.getPerson();
        this.price = priceDto.getPrice();
        this.theme = theme;
    }
}
