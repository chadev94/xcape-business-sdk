package com.chadev.xcape.api.service;

import com.chadev.xcape.core.domain.request.ReservationRequestUser;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisRepository redisRepository;

    private static final Pattern phoneNumberPattern = Pattern.compile("^\\d{3}\\d{3,4}\\d{4}$");

    private static boolean isPhoneNumber(String phoneNumber) {
        return phoneNumberPattern.matcher(phoneNumber).matches();
    }

    public boolean canReserved(String phoneNumber) {
        if (!isPhoneNumber(phoneNumber)) {
            return false;
        }

        ReservationRequestUser reservationRequestUser = redisRepository.findById(phoneNumber).orElseGet(() -> new ReservationRequestUser(phoneNumber));
        if (reservationRequestUser.getCount() > 5) {
            throw XcapeException.TO_MANY_REQUEST_FOR_RESERVATION();
        }

        reservationRequestUser.setCount(reservationRequestUser.getCount() + 1);
        redisRepository.save(reservationRequestUser);
        return true;
    }
}
