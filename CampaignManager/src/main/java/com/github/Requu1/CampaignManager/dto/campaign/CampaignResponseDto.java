package com.github.Requu1.CampaignManager.dto.campaign;

import com.github.Requu1.CampaignManager.util.Status;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResponseDto {
    private UUID id;
    private UUID productId;
    private String name;
    private List<String> keywords;
    private BigDecimal bidAmount;
    private BigDecimal campaignFund;
    private Status status;
    private String town;
    private Integer radius;
    private BigDecimal newEmeraldBalance;
}
