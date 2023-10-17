package com.chadev.xcape.core.domain.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash
@Getter @Setter
@EqualsAndHashCode
public class ReservationRequestUser {

    @Id
    private String phoneNumber;

    private int count;

    @TimeToLive
    private long timeToLive;

    public ReservationRequestUser(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.timeToLive = 600;
    }
}
