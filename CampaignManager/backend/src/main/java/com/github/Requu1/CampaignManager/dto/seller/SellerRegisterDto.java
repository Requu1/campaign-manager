package com.github.Requu1.CampaignManager.dto.seller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;


@Builder
public record SellerRegisterDto (
    @NotBlank(message = "Username is mandatory")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    String username,

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    String email,

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    String password
){}
