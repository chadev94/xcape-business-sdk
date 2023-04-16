package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BannerDto {
    private Long id;
    private String imagePath;
    private String link;
    private String description;
    private String type;
    private Integer sequence;
    private Boolean useYn;
    private Long merchantId;

    public BannerDto(Banner entity) {
        this.id = entity.getId();
        this.imagePath = entity.getImagePath();
        this.link = entity.getLink();
        this.description = entity.getDescription();
        this.type = entity.getType();
        this.sequence = entity.getSequence();
        this.useYn = entity.getUseYn();
    }
}
