package com.chadev.xcape.core.domain.entity;

import com.chadev.xcape.core.domain.dto.AbilityDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Entity
@Getter
@NoArgsConstructor
@Table(name = "ability")
public class Ability extends AuditingFields {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ability_id")
    private Long id;

    @Column(name = "ability_code")
    private String code;

    @Column(name = "ability_name")
    private String name;

    @Column(name = "ability_value")
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public Ability(AbilityDto abilityDto, Merchant merchant, Theme theme) {
        this.id = abilityDto.getId();
        this.code = abilityDto.getCode();
        this.name = abilityDto.getName();
        this.value = abilityDto.getValue();
        this.merchant = merchant;
        this.theme = theme;
    }
}
