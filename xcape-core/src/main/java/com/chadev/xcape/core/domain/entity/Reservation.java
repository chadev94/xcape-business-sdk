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
    private Merchant merchant;

    @Setter
    @Column(name = "theme_id")
    private Long themeId;

    @Setter
    @Column(name = "reservation_theme_name")
    private String themeName;

    // 날짜
    @Setter
    @Column(name = "reservation_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    // 시간
    @Setter
    @Column(name = "reservation_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalTime time;

    // 예약자 이름
    @Setter
    @Column(name = "reserved_by")
    private String reservedBy;

    // 예약자 연락처
    @Setter
    @Column(name = "phone_number")
    private String phoneNumber;

    // 인원수
    @Setter
    @Column(name = "participant_count")
    private Integer participantCount;

    // openRoom / general
    @Setter
    @Column(name = "room_type")
    private String roomType;

    // 가격
    @Setter
    @Column(name = "reservation_price")
    private Integer price;

    // 예약 여부
    @Setter
    @Column(name = "is_reserved")
    private Boolean isReserved;

    public Reservation(Merchant merchant, Long themeId, String themeName, LocalDate date, LocalTime time, String reservedBy, String phoneNumber, Integer participantCount, String roomType, Integer price, Boolean isReserved) {
        this.merchant = merchant;
        this.themeId = themeId;
        this.themeName = themeName;
        this.date = date;
        this.time = time;
        this.reservedBy = reservedBy;
        this.phoneNumber = phoneNumber;
        this.participantCount = participantCount;
        this.roomType = roomType;
        this.price = price;
        this.isReserved = isReserved;
    }

    // constructor for batch
    public Reservation(Merchant merchant, LocalDate date, LocalTime time, Long themeId, String themeName) {
        this.merchant = merchant;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
        this.themeName = themeName;
        this.isReserved = false;
    }
}
