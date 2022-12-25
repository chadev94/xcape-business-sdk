package domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Table(name = "merchant",
        indexes = {
                @Index(columnList = "id"),
                @Index(columnList = "merchant_name")
        })
@Getter
@Entity
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Setter
    @Column(name = "merchant_name")
    private String name;

    @Setter
    @Column(name = "address")
    private String address;

    public Merchant(Account account, String name, String address) {
        this.account = account;
        this.name = name;
        this.address = address;
    }

}
