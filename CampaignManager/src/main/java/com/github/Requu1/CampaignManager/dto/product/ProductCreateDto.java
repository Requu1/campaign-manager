package com.github.Requu1.CampaignManager.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateDto {
    @NotBlank(message = "Product name is mandatory")
    @Size(min = 1, max = 50, message = "Product name must be between 1 and 50 characters")
    private String name;
}
