package com.chadev.xcape.core.domain.entity;

import com.chadev.xcape.core.domain.type.AccountRole;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "account")
@AllArgsConstructor
public class Account {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_role")
    private AccountRole role;

    @OneToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;
}
