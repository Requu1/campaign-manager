package com.github.Requu1.CampaignManager.repository;

import com.github.Requu1.CampaignManager.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller,UUID> {
}
