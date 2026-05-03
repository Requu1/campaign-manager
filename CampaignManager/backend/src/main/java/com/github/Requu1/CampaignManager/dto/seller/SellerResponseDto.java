package com.github.Requu1.CampaignManager.dto.seller;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record SellerResponseDto (
    UUID id,
    String email,
    String username,
    BigDecimal emeraldBalance
){}
