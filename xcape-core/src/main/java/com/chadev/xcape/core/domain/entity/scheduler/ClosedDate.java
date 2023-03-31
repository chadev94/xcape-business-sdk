package com.chadev.xcape.core.domain.entity.scheduler;

import com.chadev.xcape.core.domain.entity.Merchant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "closed_date")
@Entity
public class ClosedDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closed_date_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(name = "closed_date")
    private LocalDate closedDate;

    public ClosedDate(Merchant merchant, LocalDate closedDate) {
        this.merchant = merchant;
        this.closedDate = closedDate;
    }
}
