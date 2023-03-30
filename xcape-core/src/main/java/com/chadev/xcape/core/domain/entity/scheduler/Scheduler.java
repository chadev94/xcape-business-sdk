package com.chadev.xcape.core.domain.entity.scheduler;

import com.chadev.xcape.core.domain.entity.Merchant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@Table(name = "scheduler")
@Entity
public class Scheduler {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduler_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "merchant_id")
    Merchant merchant;

    @Column(name = "is_awake")
    Boolean isAwake;

    @Column(name = "time")
    LocalTime time;
}
