package com.chadev.xcape.core.domain.entity.history;

import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.type.HistoryType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "reservation_history")
@Entity
public class ReservationHistory {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private HistoryType type;

    // 기록 시간
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    // 등록/수정 당시 작성자
    @Column(name = "reserved_by")
    private String reservedBy;

    // 등록/수정 작성자 연락처
    @Column(name = "phoneNumber")
    private String phoneNumber;

    // 등록/수정 당시 인원 수
    @Column(name = "participant_count")
    private Integer participantCount;

    // 등록/수정 당시 roomType - openRoom/general
    @Column(name = "roomType")
    private String roomType;

    // 등록/수정 당시 가격
    @Column(name = "price")
    private Integer price;

    // 생성자 - 등록/수정
    public ReservationHistory(Reservation reservation, HistoryType type) {
        this.type = type;
        this.reservation = reservation;
        this.dateTime = LocalDateTime.now();
        this.reservedBy = reservation.getReservedBy();
        this.phoneNumber = reservation.getPhoneNumber();
        this.participantCount = reservation.getParticipantCount();
        this.roomType = reservation.getRoomType();
        this.price = reservation.getPrice();
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
