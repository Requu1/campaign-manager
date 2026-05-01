package com.github.Requu1.CampaignManager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seller_id",nullable=false)
    private Seller seller;

    @Builder.Default
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Campaign> campaigns=new ArrayList<>();

    @Column(nullable=false)
    @Size(min = 1,max=50)
    private String name;

    public void addCampaign(Campaign campaign) {this.campaigns.add(campaign);}
    public void deleteCampaign(Campaign campaign) {this.campaigns.remove(campaign);}

}
