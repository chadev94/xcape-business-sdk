package com.chadev.xcape.api.repository.mapping;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationInfo {

    Long getId();

    Long getThemeId();

    Long getMerchantId();

    LocalDate getDate();

    LocalTime getTime();

    Boolean getIsReserved();

}
