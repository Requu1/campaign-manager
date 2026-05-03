package com.github.Requu1.CampaignManager.repository;

import com.github.Requu1.CampaignManager.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product,UUID> {
    List<Product> findAllBySellerId(UUID sellerId);
    boolean existsByNameAndSellerId(String name, UUID sellerId);
}
