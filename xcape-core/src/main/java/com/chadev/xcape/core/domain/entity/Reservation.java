package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Table(name = "reservation")
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    Theme theme;

    // 예약 시간
    @Setter @Column(name = "start_time")
    private LocalDateTime startTime;

    // 예약자 이름
    @Setter @Column(name = "reservation_name")
    private String name;

    // 예약자 핸드폰 번호
    @Setter @Column(name = "phone_number")
    private String phoneNumber;

    // 인원
    @Setter @Column(name = "head_count")
    private Integer headCount;

    // 가격
    @Setter @Column(name = "price")
    private Integer price;

    // 예약 여부
    @Setter @Column(name = "is_reserved")
    private Boolean isReserved;

    public Reservation(Theme theme, LocalDateTime startTime, String name, String phoneNumber, Integer headCount, Integer price, Boolean isReserved) {
        this.theme = theme;
        this.startTime = startTime;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.headCount = headCount;
        this.price = price;
        this.isReserved = isReserved;
    }
}
