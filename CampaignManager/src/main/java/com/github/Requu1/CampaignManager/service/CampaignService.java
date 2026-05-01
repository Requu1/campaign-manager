package com.github.Requu1.CampaignManager.service;

import com.github.Requu1.CampaignManager.exception.CampaignNotFoundException;
import com.github.Requu1.CampaignManager.model.Campaign;
import com.github.Requu1.CampaignManager.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public List<Campaign> getCampaignsForProduct(UUID productId) {
        return campaignRepository.findAllByProductId(productId);
    }

    public void removeCampaign(UUID id) {
        campaignRepository.delete(findById(id));
    }

    private Campaign findById(UUID id) {
        return campaignRepository.findById(id)
                .orElseThrow(()->new CampaignNotFoundException(String.format("Campaign with ID:%s not found",id)));
    }
}
