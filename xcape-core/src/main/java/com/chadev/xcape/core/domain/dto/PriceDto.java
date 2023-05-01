package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Price;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {

    private Long id;

    private Integer person;

    private Integer price;

    private Long themeId;

    public PriceDto(Price entity) {
        this.id = entity.getId();
        this.person = entity.getPerson();
        this.price = entity.getPrice();
        this.themeId = entity.getTheme().getId();
    }

    public static PriceDto from(Price entity) {
        return new PriceDto(
                entity.getId(),
                entity.getPerson(),
                entity.getPrice(),
                entity.getTheme().getId()
        );
    }
}
