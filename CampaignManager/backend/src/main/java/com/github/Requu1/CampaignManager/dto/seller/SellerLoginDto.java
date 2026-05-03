package com.github.Requu1.CampaignManager.dto.seller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SellerLoginDto (
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    String email,

    @NotBlank(message = "Password is mandatory")
    String password
){}
