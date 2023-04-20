package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.ReservationAuthentication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ReservationAuthenticationDto {

    private String requestId;

    private Long reservationId;

    private String authenticationNumber;

    private LocalDateTime registeredAt;

    public static ReservationAuthenticationDto from(ReservationAuthentication entity) {
        return new ReservationAuthenticationDto(
                entity.getRequestId(),
                entity.getReservation().getId(),
                entity.getAuthenticationNumber(),
                entity.getRegisteredAt()
        );
    }

    public static ReservationAuthenticationDto fromResponseClient(ReservationAuthentication entity) {
        return ReservationAuthenticationDto.builder()
                .requestId(entity.getRequestId())
                .reservationId(entity.getReservation().getId())
                .registeredAt(entity.getRegisteredAt()).build();
    }
}
