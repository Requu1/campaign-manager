package com.github.Requu1.CampaignManager.dto.campaign;

import com.github.Requu1.CampaignManager.util.Status;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CampaignResponseDto (
    UUID id,
    UUID productId,
    String name,
    List<String> keywords,
    BigDecimal bidAmount,
    BigDecimal campaignFund,
    Status status,
    String town,
    Integer radius,
    BigDecimal newEmeraldBalance
){}
