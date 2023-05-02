package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Reservation;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReservationDto {

    // 기본값
    private String id;

    private Long seq;

    private String merchantName;

    private Long themeId;

    private String themeName;

    private LocalDate date;

    private String time;

    private Boolean isReserved;

    // 예약 등록 시 설정되는 값
    private String reservedBy;

    private String phoneNumber;

    private Integer participantCount;

    private Integer price;

    private String reservationHistoryId;

    public ReservationDto(Reservation entity) {
        this.id = entity.getId();
        this.seq = entity.getSeq();
        this.merchantName = entity.getMerchantName();
        this.themeId = entity.getThemeId();
        this.themeName = entity.getThemeName();
        this.date = entity.getDate();
        this.time = entity.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.isReserved = entity.getIsReserved();
        if (entity.getIsReserved()){
            this.reservedBy = entity.getReservedBy();
            this.phoneNumber = entity.getPhoneNumber();
            this.participantCount = entity.getParticipantCount();
            this.price = entity.getPrice();
        }
    }

    // for fake reservation
    public static ReservationDto fake(Reservation entity) {
        return new ReservationDto().builder()
                .id(entity.getId())
                .seq(entity.getSeq())
                .merchantName(entity.getMerchantName())
                .themeId(entity.getThemeId())
                .themeName(entity.getThemeName())
                .date(entity.getDate())
                .time(entity.getTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .isReserved(true)
                .reservedBy("XCAPE")
                .phoneNumber("01000000000")
                .participantCount(2)
                .price(0)
                .reservationHistoryId(UUID.randomUUID().toString())
                .build();
    }


}
