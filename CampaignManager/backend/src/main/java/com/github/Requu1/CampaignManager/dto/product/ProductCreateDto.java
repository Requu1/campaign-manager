package com.github.Requu1.CampaignManager.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ProductCreateDto (
    @NotBlank(message = "Product name is mandatory")
    @Size(min = 1, max = 20, message = "Product name must be between 1 and 20 characters")
    String name
){}
