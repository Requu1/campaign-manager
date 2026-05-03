package com.github.Requu1.CampaignManager.dto.product;

import lombok.*;

import java.util.UUID;


@Builder
public record ProductResponseDto (
    UUID id,
    String name
){}
