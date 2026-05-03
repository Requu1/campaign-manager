package com.github.Requu1.CampaignManager.service;

import com.github.Requu1.CampaignManager.dto.campaign.CampaignCreateDto;
import com.github.Requu1.CampaignManager.dto.campaign.CampaignResponseDto;
import com.github.Requu1.CampaignManager.exception.CampaignNotFoundException;
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

import static com.github.Requu1.CampaignManager.util.DictionaryUtil.areKeywordsValid;
import static com.github.Requu1.CampaignManager.util.DictionaryUtil.isTownValid;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final ProductService productService;
    private final SellerService sellerService;

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

        if(campaignRepository.existsByNameAndProductId(campaignCreateDto.name(),productId)){
            throw new IllegalArgumentException("Campaign with the same name already exists");
        }

        if(!isTownValid(campaignCreateDto.town())){
            throw new IllegalArgumentException("Town is not valid");
        }

        if(!areKeywordsValid(campaignCreateDto.keywords())){
            throw new IllegalArgumentException("Keywords are not valid");
        }

        BigDecimal requiredFund = campaignCreateDto.campaignFund();
        sellerService.chargeFunds(sellerId,requiredFund);

        Campaign campaign = Campaign.builder()
                .product(product)
                .name(campaignCreateDto.name())
                .keywords(campaignCreateDto.keywords())
                .bidAmount(campaignCreateDto.bidAmount())
                .campaignFund(campaignCreateDto.campaignFund())
                .status(campaignCreateDto.status())
                .town(campaignCreateDto.town())
                .radius(campaignCreateDto.radius())
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

        Optional<Campaign> existingCampaign=campaignRepository.findCampaignByNameAndProductId(campaignCreateDto.name(),productId);
        if (existingCampaign.isPresent() && !existingCampaign.get().getId().equals(campaignId)) {
            throw new IllegalArgumentException("Campaign with the same name already exists");
        }

        if(!isTownValid(campaignCreateDto.town())){
            throw new IllegalArgumentException("Town is not valid");
        }

        if(!areKeywordsValid(campaignCreateDto.keywords())){
            throw new IllegalArgumentException("Keywords are not valid");
        }

        updateCampaignFund(campaign, campaignCreateDto.campaignFund());

        campaign.setName(campaignCreateDto.name());
        campaign.setKeywords(campaignCreateDto.keywords());
        campaign.setBidAmount(campaignCreateDto.bidAmount());
        campaign.setCampaignFund(campaignCreateDto.campaignFund());
        campaign.setStatus(campaignCreateDto.status());
        campaign.setTown(campaignCreateDto.town());
        campaign.setRadius(campaignCreateDto.radius());

        return mapToDto(campaign);
    }

    @Transactional
    public void removeCampaign(UUID sellerId, UUID productId, UUID campaignId) {
        productService.findOwnedProduct(sellerId, productId);
        Campaign campaign = findById(campaignId);

        if (!campaign.getProduct().getId().equals(productId)) {
            throw new NoPermissionException("Campaign does not belong to this product");
        }

        sellerService.refundFunds(sellerId,campaign.getCampaignFund());
        campaignRepository.delete(campaign);
    }

    private void updateCampaignFund(Campaign campaign, BigDecimal newFund) {
        BigDecimal oldFund = campaign.getCampaignFund();
        Seller seller = campaign.getProduct().getSeller();
        BigDecimal difference = newFund.subtract(oldFund);

        if (difference.compareTo(BigDecimal.ZERO) > 0) {
            sellerService.chargeFunds(seller.getId(), difference);
        } else if (difference.compareTo(BigDecimal.ZERO) < 0) {
            sellerService.refundFunds(seller.getId(), difference.abs());
        }
    }

    private Campaign findById(UUID id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(String.format("Campaign with ID:%s not found", id)));
    }
}