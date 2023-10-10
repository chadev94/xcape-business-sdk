package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "hint")
public class Hint extends AuditingFields {

    @Setter(AccessLevel.NONE)
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hint_id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "message1")
    private String message1;

    @Column(name = "message2")
    private String message2;

    @Column(name = "is_used")
    private boolean isUsed;

    @Column(name = "registered_by")
    private String registeredBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

}

