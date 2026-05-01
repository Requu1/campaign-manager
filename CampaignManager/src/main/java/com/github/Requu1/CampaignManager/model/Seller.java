package com.github.Requu1.CampaignManager.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable=false,unique = true)
    private String username;

    @Column(nullable = false,unique = true)
    private String email;

    @Builder.Default
    @Column(nullable = false,precision =10, scale =2)
    private BigDecimal emeraldBalance=BigDecimal.ZERO;

    @OneToMany(mappedBy = "seller",orphanRemoval = true)
    @Builder.Default
    private List<Product> products=new ArrayList<>();

}
