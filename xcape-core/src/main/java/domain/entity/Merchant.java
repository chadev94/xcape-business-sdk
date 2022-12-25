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
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Setter
    @Column(name = "merchant_name")
    private String name;

    @Setter
    @Column(name = "address")
    private String address;

    public Merchant(Admin admin, String name, String address) {
        this.admin = admin;
        this.name = name;
        this.address = address;
    }

}
