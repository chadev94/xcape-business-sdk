package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Price;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PriceDto {

    private Long id;

    private Integer person;

    private Integer price;

    private String type;

    private Long themeId;

    public PriceDto(Price price) {
        this.id = price.getId();
        this.person = price.getPerson();
        this.price = price.getPerson();
        this.type = price.getType();
        this.themeId = price.getTheme().getId();
    }
}
