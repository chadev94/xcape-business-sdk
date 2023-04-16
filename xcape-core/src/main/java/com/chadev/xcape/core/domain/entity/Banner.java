package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "banner")
public class Banner extends AuditingFields {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long id;

    @Column(name = "banner_image_path", length = 500)
    private String imagePath;

    @Column(name = "link", length = 300)
    private String link;

    @Column(name = "description", length = 300)
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "use_yn")
    private Boolean useYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

//    @Builder
    public Banner(String imagePath, String link, String description, String type, Integer sequence, Boolean useYn, Merchant merchant) {
        this.imagePath = imagePath;
        this.link = link;
        this.description = description;
        this.type = type;
        this.sequence = sequence;
        this.useYn = useYn;
        this.merchant = merchant;
    }
}
