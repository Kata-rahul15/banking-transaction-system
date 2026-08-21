package com.example.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema(description = "Request payload for deposit or withdrawal operations")
public class AmountRequest {

    @Schema(description = "Amount to deposit or withdraw", example = "100.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String amount;

    public String getAmount() {
        return amount;
    }
}