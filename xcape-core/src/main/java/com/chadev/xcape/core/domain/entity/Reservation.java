package com.chadev.xcape.core.domain.entity;

import com.chadev.xcape.core.domain.type.RoomType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "reservation")
@Entity
@Builder
@AllArgsConstructor
public class Reservation extends AuditingFields {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_seq", nullable = false)
    private Long seq;

    @Setter(AccessLevel.NONE)
    @Column(name = "reservation_id", nullable = false)
    private String id;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "reservation_merchant_name")
    private String merchantName;

    @Column(name = "theme_id")
    private Long themeId;

    @Column(name = "reservation_theme_name")
    private String themeName;

    // 날짜
    @Column(name = "reservation_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    // 시간
    @Column(name = "reservation_time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    // 예약자 이름
    @Column(name = "reserved_by")
    private String reservedBy;

    // 예약자 연락처
    @Column(name = "phone_number")
    private String phoneNumber;

    // 인원수
    @Column(name = "participant_count")
    private Integer participantCount;

    // 가격
    @Column(name = "reservation_price")
    private Integer price;

    // 예약 여부
    @Column(name = "is_reserved")
    private Boolean isReserved;

    // 가예약 해제 시간
    @Column(name = "unreserved_time")
    private LocalTime unreservedTime;

    @Column(name = "room_type")
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    public Reservation(Merchant merchant, Long themeId, String themeName, LocalDate date, LocalTime time, String reservedBy, String phoneNumber, Integer participantCount, Integer price, Boolean isReserved) {
        this.merchantName = merchant.getName();
        this.themeId = themeId;
        this.themeName = themeName;
        this.date = date;
        this.time = time;
        this.reservedBy = reservedBy;
        this.phoneNumber = phoneNumber;
        this.participantCount = participantCount;
        this.price = price;
        this.isReserved = isReserved;
    }

    // constructor for batch
    public Reservation(Merchant merchant, String id, LocalDate date, LocalTime time, Long themeId, String themeName) {
        this.merchantName = merchant.getName();
        this.id = id;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
        this.themeName = themeName;
        this.isReserved = false;
    }
}
