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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    Merchant merchant;

    // 예약 시간
    @Setter
    @Column(name = "start_time")
    private String startTime;

    @Setter
    @Column(name = "date")
    private String date;

    // 예약자 이름
    @Setter
    @Column(name = "reserved_by")
    private String reservedBy;

    // 예약자 핸드폰 번호
    @Setter
    @Column(name = "phone_number")
    private String phoneNumber;

    // 인원
    @Setter
    @Column(name = "count")
    private Integer count;

    // 예약 여부
    @Setter
    @Column(name = "is_reserved")
    private Boolean isReserved;

    public Reservation(Theme theme, Merchant merchant, String startTime, String date, String reservedBy, String phoneNumber, Integer count, Boolean isReserved) {
        this.theme = theme;
        this.merchant = merchant;
        this.startTime = startTime;
        this.date = date;
        this.reservedBy = reservedBy;
        this.phoneNumber = phoneNumber;
        this.count = count;
        this.isReserved = isReserved;
    }
}
