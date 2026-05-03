package com.github.Requu1.CampaignManager.repository;

import com.github.Requu1.CampaignManager.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign,UUID> {
    List<Campaign> findAllByProductId(UUID productId);
    Optional<Campaign> findCampaignByNameAndProductId(String name, UUID productId);
    boolean existsByNameAndProductId(String name, UUID productId);
}
