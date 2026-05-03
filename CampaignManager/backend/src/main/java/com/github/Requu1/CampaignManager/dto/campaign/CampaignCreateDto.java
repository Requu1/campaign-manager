package com.github.Requu1.CampaignManager.dto.campaign;

import com.github.Requu1.CampaignManager.util.Status;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CampaignCreateDto (
    @NotBlank(message = "Campaign name is mandatory")
    @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
    String name,

    @NotEmpty(message = "At least one keyword is required")
    List<@NotBlank(message = "Keyword cannot be blank") String> keywords,

    @NotNull(message = "Bid amount is mandatory")
    @DecimalMin(value = "10.00", message = "Minimum bid amount is 10")
    BigDecimal bidAmount,

    @NotNull(message = "Campaign fund is mandatory")
    @DecimalMin(value = "50", message = "Minimum campaign fund is 50")
    BigDecimal campaignFund,

    @NotNull(message = "Status is mandatory")
    Status status,

    @NotBlank(message = "Town is mandatory")
    String town,

    @NotNull(message = "Radius is mandatory")
    @Positive(message = "Radius must be a positive number")
    Integer radius
){}
