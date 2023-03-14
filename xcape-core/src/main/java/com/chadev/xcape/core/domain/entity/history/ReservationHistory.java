package com.chadev.xcape.core.domain.entity.history;

import com.chadev.xcape.core.domain.entity.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "reservation_history")
@Entity
public class ReservationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "reservation_history_info")
    private String info;

    @Column(name = "reservation_history_type")
    @Enumerated(EnumType.STRING)
    private HistoryType type;

    @Column(name = "reservation_history_date_time")
    private LocalDateTime dateTime;

    public ReservationHistory(Reservation reservation, String info, HistoryType type) {
        this.reservation = reservation;
        this.info = info;
        this.type = type;
        this.dateTime = LocalDateTime.now();
    }

    public static ReservationHistory register(Reservation reservation) {
        return new ReservationHistory(reservation, reservation.getPhoneNumber(), HistoryType.REGISTER);
    }

    public static ReservationHistory modify(Reservation reservation) {
        return new ReservationHistory(reservation, reservation.getPhoneNumber(), HistoryType.MODIFY);
    }

    public static ReservationHistory cancel(Reservation reservation) {
        return new ReservationHistory(reservation, reservation.getPhoneNumber(), HistoryType.CANCEL);
    }
}
