package com.github.Requu1.CampaignManager.dto;

import com.github.Requu1.CampaignManager.util.Status;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CampaignCreateDto {
    @NotBlank(message = "Campaign name is mandatory")
    @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    private String name;

    @NotEmpty(message = "At least one keyword is required")
    private List<@NotBlank(message = "Keyword cannot be blank") String> keywords;

    @NotNull(message = "Bid amount is mandatory")
    @DecimalMin(value = "100.00", message = "Minimum bid amount is 100")
    private BigDecimal bidAmount;

    @NotNull(message = "Campaign fund is mandatory")
    @DecimalMin(value = "10", message = "Minimum campaign fund is 10")
    private BigDecimal campaignFund;

    @NotNull(message = "Status is mandatory")
    private Status status;

    @NotBlank(message = "Town is mandatory")
    private String town;

    @NotNull(message = "Radius is mandatory")
    @Positive(message = "Radius must be a positive number")
    private Integer radius;
}
