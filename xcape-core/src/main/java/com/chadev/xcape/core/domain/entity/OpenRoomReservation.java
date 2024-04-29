package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "open_room_reservation")
@NoArgsConstructor
public class OpenRoomReservation {

	@Setter(AccessLevel.NONE)
	@NotNull
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "open_room_reservation_id")
	private Long id;

	// 예약자 이름
	@Column(name = "reserved_by")
	private String reservedBy;

	// 예약자 연락처
	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "participant_count")
	private Integer participantCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

}
