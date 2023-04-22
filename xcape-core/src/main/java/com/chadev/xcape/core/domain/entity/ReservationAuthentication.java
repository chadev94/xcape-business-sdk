package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "reservation_authentication")
@Entity
public class ReservationAuthentication extends AuditingFields {
    @Id
    @Column(name = "request_id", nullable = false)
    private String requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_seq")
    private Reservation reservation;

    @Column(name = "authentication_number", nullable = false)
    private String authenticationNumber;
}
