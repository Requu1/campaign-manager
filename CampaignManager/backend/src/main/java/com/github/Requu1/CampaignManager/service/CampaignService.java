package com.github.Requu1.CampaignManager.service;

import com.github.Requu1.CampaignManager.dto.campaign.CampaignCreateDto;
import com.github.Requu1.CampaignManager.dto.campaign.CampaignResponseDto;
import com.github.Requu1.CampaignManager.exception.CampaignNotFoundException;
import com.github.Requu1.CampaignManager.exception.InsufficientFundsException;
import com.github.Requu1.CampaignManager.exception.NoPermissionException;
import com.github.Requu1.CampaignManager.model.Campaign;
import com.github.Requu1.CampaignManager.model.Product;
import com.github.Requu1.CampaignManager.model.Seller;
import com.github.Requu1.CampaignManager.repository.CampaignRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final ProductService productService;

    private CampaignResponseDto mapToDto(Campaign campaign) {
        return CampaignResponseDto.builder()
                .id(campaign.getId())
                .productId(campaign.getProduct().getId())
                .name(campaign.getName())
                .keywords(campaign.getKeywords())
                .bidAmount(campaign.getBidAmount())
                .campaignFund(campaign.getCampaignFund())
                .status(campaign.getStatus())
                .town(campaign.getTown())
                .radius(campaign.getRadius())
                .newEmeraldBalance(campaign.getProduct().getSeller().getEmeraldBalance())
                .build();
    }

    public List<CampaignResponseDto> getCampaignsForProduct(UUID sellerId, UUID productId) {
        productService.findOwnedProduct(sellerId, productId);
        return campaignRepository.findAllByProductId(productId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public CampaignResponseDto createCampaign(UUID sellerId, UUID productId, CampaignCreateDto campaignCreateDto) {
        Product product = productService.findOwnedProduct(sellerId, productId);
        Seller seller = product.getSeller();
        BigDecimal requiredFund = campaignCreateDto.getCampaignFund();

        if(campaignRepository.existsByNameAndProductId(campaignCreateDto.getName(),productId)){
            throw new IllegalArgumentException("Campaign with the same name already exists");
        }

        if (seller.getEmeraldBalance().compareTo(requiredFund) < 0) {
            throw new InsufficientFundsException("Not enough funds on Emerald account");
        }else{
            seller.setEmeraldBalance(seller.getEmeraldBalance().subtract(requiredFund));
        }

        Campaign campaign = Campaign.builder()
                .product(product)
                .name(campaignCreateDto.getName())
                .keywords(campaignCreateDto.getKeywords())
                .bidAmount(campaignCreateDto.getBidAmount())
                .campaignFund(campaignCreateDto.getCampaignFund())
                .status(campaignCreateDto.getStatus())
                .town(campaignCreateDto.getTown())
                .radius(campaignCreateDto.getRadius())
                .build();

        campaignRepository.save(campaign);
        return mapToDto(campaign);
    }

    @Transactional
    public CampaignResponseDto updateCampaign(UUID sellerId, UUID productId, UUID campaignId, CampaignCreateDto campaignCreateDto) {
        productService.findOwnedProduct(sellerId, productId);

        Campaign campaign = findById(campaignId);

        if (!campaign.getProduct().getId().equals(productId)) {
            throw new NoPermissionException("Campaign does not belong to this product");
        }

        Optional<Campaign> existingCampaign=campaignRepository.findCampaignByNameAndProductId(campaignCreateDto.getName(),productId);
        if (existingCampaign.isPresent() && !existingCampaign.get().getId().equals(campaignId)) {
            throw new IllegalArgumentException("Campaign with the same name already exists");
        }

        updateCampaignFund(campaign, campaignCreateDto.getCampaignFund());

        campaign.setName(campaignCreateDto.getName());
        campaign.setKeywords(campaignCreateDto.getKeywords());
        campaign.setBidAmount(campaignCreateDto.getBidAmount());
        campaign.setCampaignFund(campaignCreateDto.getCampaignFund());
        campaign.setStatus(campaignCreateDto.getStatus());
        campaign.setTown(campaignCreateDto.getTown());
        campaign.setRadius(campaignCreateDto.getRadius());

        return mapToDto(campaign);
    }

    @Transactional
    public void removeCampaign(UUID sellerId, UUID productId, UUID campaignId) {
        productService.findOwnedProduct(sellerId, productId);
        Campaign campaign = findById(campaignId);

        if (!campaign.getProduct().getId().equals(productId)) {
            throw new NoPermissionException("Campaign does not belong to this product");
        }

        Seller seller = campaign.getProduct().getSeller();
        seller.setEmeraldBalance(seller.getEmeraldBalance().add(campaign.getCampaignFund()));
        campaignRepository.delete(campaign);
    }

    private void updateCampaignFund(Campaign campaign, BigDecimal newFund) {
        BigDecimal oldFund = campaign.getCampaignFund();
        Seller seller = campaign.getProduct().getSeller();
        BigDecimal difference = newFund.subtract(oldFund);

        if (difference.compareTo(BigDecimal.ZERO) > 0) {
            if (seller.getEmeraldBalance().compareTo(difference) < 0) {
                throw new InsufficientFundsException("Not enough funds on Emerald account");
            }
            seller.setEmeraldBalance(seller.getEmeraldBalance().subtract(difference));
        } else if (difference.compareTo(BigDecimal.ZERO) < 0) {
            seller.setEmeraldBalance(seller.getEmeraldBalance().add(difference.abs()));
        }
    }

    private Campaign findById(UUID id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(String.format("Campaign with ID:%s not found", id)));
    }
}