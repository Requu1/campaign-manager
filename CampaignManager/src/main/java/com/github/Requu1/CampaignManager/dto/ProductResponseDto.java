package com.github.Requu1.CampaignManager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;


@Getter
@Setter
@Builder
public class ProductResponseDto {
    private UUID id;
    private String name;
}
