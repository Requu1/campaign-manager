package com.github.Requu1.CampaignManager.model;

import com.github.Requu1.CampaignManager.util.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id",nullable=false)
    private Product product;

    @Column(nullable=false)
    @Size(min=5,max=30)
    private String name;

    @Column(nullable=false,precision=10,scale=2)
    private BigDecimal bidAmount;

    @Column(nullable=false,precision=10,scale=2)
    private BigDecimal campaignFund;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable=false)
    private String town;

    @ElementCollection
    @CollectionTable(name="campaign_keywords",joinColumns = @JoinColumn(name="campaign_id"))
    @Builder.Default
    @Column(nullable=false)
    private List<String> keywords=new ArrayList<>();

    @Column(nullable=false)
    @Positive
    private Integer radius;
}
