package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

    public Price(Theme theme, Integer person, Integer price, String type) {
        this.theme = theme;
        this.person = person;
        this.price = price;
        this.type = type;
    }
}
