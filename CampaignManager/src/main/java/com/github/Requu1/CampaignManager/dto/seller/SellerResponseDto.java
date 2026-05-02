package com.github.Requu1.CampaignManager.dto.seller;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponseDto {
    private UUID id;
    private String email;
    private String username;
    private BigDecimal emeraldBalance;
}
