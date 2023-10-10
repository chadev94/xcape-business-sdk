package com.chadev.xcape.api.service;

import com.chadev.xcape.core.domain.request.ReservationRequestUser;
import com.chadev.xcape.core.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisRepository redisRepository;

    public boolean isOverReservationCount(String phoneNumber) {
        ReservationRequestUser reservationRequestUser = redisRepository.findById(phoneNumber)
                .orElse(new ReservationRequestUser(phoneNumber));

        if (reservationRequestUser.getCount() > 4) {
            return false;
        }

        reservationRequestUser.setCount(reservationRequestUser.getCount() + 1);
        redisRepository.save(reservationRequestUser);

        return true;
    }
}
