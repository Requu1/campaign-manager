package com.github.Requu1.CampaignManager.controller;

import com.github.Requu1.CampaignManager.dto.CampaignCreateDto;
import com.github.Requu1.CampaignManager.dto.CampaignResponseDto;
import com.github.Requu1.CampaignManager.service.CampaignService;
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
    public ResponseEntity<List<CampaignResponseDto>> getCampaigns(@RequestHeader("SellerId") UUID sellerId,
                                                                  @PathVariable UUID productId) {
        return ResponseEntity.ok(campaignService.getCampaignsForProduct(sellerId, productId));
    }

    @PostMapping
    public ResponseEntity<CampaignResponseDto> createCampaign(@RequestHeader("SellerId") UUID sellerId,
                                                              @PathVariable UUID productId,
                                                              @Valid @RequestBody CampaignCreateDto campaignCreateDto) {
        CampaignResponseDto createdCampaign=campaignService.createCampaign(sellerId,productId,campaignCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCampaign);
    }

    @PutMapping("/{campaignId}")
    public ResponseEntity<CampaignResponseDto> updateCampaign(@RequestHeader("SellerId") UUID sellerId,
                                                              @PathVariable UUID productId,
                                                              @PathVariable UUID campaignId,
                                                              @Valid @RequestBody CampaignCreateDto campaignCreateDto){
        CampaignResponseDto updatedCampaign=campaignService.updateCampaign(sellerId,productId,campaignId,campaignCreateDto);
        return ResponseEntity.ok(updatedCampaign);
    }

    @DeleteMapping("/{campaignId}")
    public ResponseEntity<Void> deleteCampaign(@RequestHeader("SellerId") UUID sellerId,
                                               @PathVariable UUID productId,
                                               @PathVariable UUID campaignId) {
        campaignService.removeCampaign(sellerId,productId,campaignId);
        return ResponseEntity.noContent().build();
    }
}