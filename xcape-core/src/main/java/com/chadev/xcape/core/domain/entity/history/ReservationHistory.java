package com.chadev.xcape.core.domain.entity.history;

import com.chadev.xcape.core.domain.entity.AuditingFields;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.type.HistoryType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "reservation_history")
@Entity
public class ReservationHistory extends AuditingFields {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_history_seq")
    private Long seq;

    @Column(name = "reservation_history_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_seq")
    private Reservation reservation;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private HistoryType type;

    // 등록/수정 당시 작성자
    @Column(name = "reserved_by")
    private String reservedBy;

    // 등록/수정 작성자 연락처
    @Column(name = "phone_number")
    private String phoneNumber;

    // 등록/수정 당시 인원 수
    @Column(name = "participant_count")
    private Integer participantCount;

    // 등록/수정 당시 roomType - openRoom/general
    @Column(name = "room_type")
    private String roomType;

    // 등록/수정 당시 가격
    @Column(name = "price")
    private Integer price;

    @Column(name = "reservation_history_theme_name")
    private String themeName;

    // 날짜
    @Column(name = "reservation_history_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    // 시간
    @Column(name = "reservation_history_time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    // 생성자 - 등록/수정
    public ReservationHistory(Reservation reservation, HistoryType type) {
        this.id = LocalDate.now() + "-" + UUID.randomUUID();
        this.type = type;
        this.reservation = reservation;
        this.reservedBy = reservation.getReservedBy();
        this.phoneNumber = reservation.getPhoneNumber();
        this.participantCount = reservation.getParticipantCount();
        this.roomType = reservation.getRoomType();
        this.price = reservation.getPrice();
        this.themeName = reservation.getThemeName();
        this.date = reservation.getDate();
        this.time = reservation.getTime();
    }

    public static ReservationHistory register(Reservation reservation) {
        return new ReservationHistory(reservation, HistoryType.REGISTER);
    }

    public static ReservationHistory modify(Reservation reservation) {
        return new ReservationHistory(reservation, HistoryType.MODIFY);
    }

    public static ReservationHistory cancel(Reservation reservation) {
        return new ReservationHistory(reservation, HistoryType.CANCEL);
    }
}
