package com.github.Requu1.CampaignManager.controller;

import com.github.Requu1.CampaignManager.dto.campaign.CampaignCreateDto;
import com.github.Requu1.CampaignManager.dto.campaign.CampaignResponseDto;
import com.github.Requu1.CampaignManager.service.CampaignService;
import jakarta.servlet.http.HttpSession;
import static com.github.Requu1.CampaignManager.util.SessionUtil.getSellerIdFromSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;

    @GetMapping
    public ResponseEntity<List<CampaignResponseDto>> getCampaigns(HttpSession session,
                                                                  @PathVariable UUID productId) {
        UUID sellerId=getSellerIdFromSession(session);
        return ResponseEntity.ok(campaignService.getCampaignsForProduct(sellerId, productId));
    }

    @PostMapping
    public ResponseEntity<CampaignResponseDto> createCampaign(HttpSession session,
                                                              @PathVariable UUID productId,
                                                              @Valid @RequestBody CampaignCreateDto campaignCreateDto) {
        UUID sellerId=getSellerIdFromSession(session);
        CampaignResponseDto createdCampaign=campaignService.createCampaign(sellerId,productId,campaignCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCampaign);
    }

    @PutMapping("/{campaignId}")
    public ResponseEntity<CampaignResponseDto> updateCampaign(HttpSession session,
                                                              @PathVariable UUID productId,
                                                              @PathVariable UUID campaignId,
                                                              @Valid @RequestBody CampaignCreateDto campaignCreateDto){
        UUID sellerId=getSellerIdFromSession(session);
        CampaignResponseDto updatedCampaign=campaignService.updateCampaign(sellerId,productId,campaignId,campaignCreateDto);
        return ResponseEntity.ok(updatedCampaign);
    }

    @DeleteMapping("/{campaignId}")
    public ResponseEntity<Void> deleteCampaign(HttpSession session,
                                               @PathVariable UUID productId,
                                               @PathVariable UUID campaignId) {
        UUID sellerId=getSellerIdFromSession(session);
        campaignService.removeCampaign(sellerId,productId,campaignId);
        return ResponseEntity.noContent().build();
    }
}