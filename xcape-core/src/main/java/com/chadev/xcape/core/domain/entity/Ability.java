package com.chadev.xcape.core.domain.entity;

import com.chadev.xcape.core.domain.dto.AbilityDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ability")
public class Ability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ability_id")
    private Long id;

    @Setter
    @Column(name = "ability_code")
    private String code;

    @Setter
    @Column(name = "ability_name")
    private String name;

    @Setter
    @Column(name = "ability_value")
    private Integer value;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public Ability(AbilityDto abilityDto, Theme theme) {
        this.id = abilityDto.getId();
        this.code = abilityDto.getCode();
        this.name = abilityDto.getName();
        this.value = abilityDto.getValue();
        this.theme = theme;
    }
}
