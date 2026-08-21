package com.example.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema(description = "Request payload for creating a new bank account")
public class CreateAccountRequest {

    @Schema(description = "Full name of the account holder", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Email address of the account holder", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Initial balance amount as string to avoid floating point issues", example = "500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String initialBalance;
}