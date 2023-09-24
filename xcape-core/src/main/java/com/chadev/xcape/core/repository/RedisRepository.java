package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.request.ReservationRequestUser;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<ReservationRequestUser, String> {
}
