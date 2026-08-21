package com.example.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema(description = "Request payload for fund transfer between two accounts")
public class TransferRequest {

    @Schema(description = "ID of the source account", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fromAccountId;

    @Schema(description = "ID of the destination account", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long toAccountId;

    @Schema(description = "Amount to transfer", example = "250.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String amount;
}