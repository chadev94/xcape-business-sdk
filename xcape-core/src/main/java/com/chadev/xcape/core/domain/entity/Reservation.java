package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

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
    @JoinColumn(name = "merchant_id")
    Merchant merchant;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    Theme theme;

    @Setter
    @Column(name = "reservation_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    // 예약 시간
    @Setter
    @Column(name = "reservation_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalTime time;

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
    @Column(name = "participant_count")
    private Integer participantCount;

    @Setter
    @Column(name = "price")
    private Integer price;

    // 예약 여부
    @Setter
    @Column(name = "is_reserved")
    private Boolean isReserved;

    public Reservation(Merchant merchant, Theme theme, LocalDate date, LocalTime time, String reservedBy, String phoneNumber, Integer participantCount, Integer price, Boolean isReserved) {
        this.merchant = merchant;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.reservedBy = reservedBy;
        this.phoneNumber = phoneNumber;
        this.participantCount = participantCount;
        this.price = price;
        this.isReserved = isReserved;
    }

    // constructor for batch
    public Reservation(Merchant merchant, Theme theme, LocalDate date, LocalTime time) {
        this.merchant = merchant;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.isReserved = false;
    }
}
