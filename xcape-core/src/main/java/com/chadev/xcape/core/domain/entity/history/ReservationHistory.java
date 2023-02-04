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

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "registered_by")
    private String registeredBy;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "canceled_by")
    private String canceledBy;

    public ReservationHistory(Reservation reservation, LocalDateTime registeredAt, String registeredBy, LocalDateTime modifiedAt, String modifiedBy, LocalDateTime canceledAt, String canceledBy) {
        this.reservation = reservation;
        this.registeredAt = registeredAt;
        this.registeredBy = registeredBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
        this.canceledAt = canceledAt;
        this.canceledBy = canceledBy;
    }

    public static ReservationHistory register(Reservation reservation) {
        return new ReservationHistory(reservation, LocalDateTime.now(), reservation.getReservedBy(), null, null, null, null);
    }

    public static ReservationHistory modify(Reservation reservation) {
        return new ReservationHistory(reservation, null, null, LocalDateTime.now(), reservation.getReservedBy(), null, null);
    }

    public static ReservationHistory cancel(Reservation reservation) {
        return new ReservationHistory(reservation, null, null, null, null, LocalDateTime.now(), reservation.getReservedBy());
    }
}
