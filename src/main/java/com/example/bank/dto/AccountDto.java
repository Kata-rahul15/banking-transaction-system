package com.example.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Account data transfer object representing bank account details")
public class AccountDto {

    @Schema(description = "Unique identifier of the account", example = "1")
    private Long id;

    @Schema(description = "Full name of the account holder", example = "John Doe")
    private String name;

    @Schema(description = "Email address of the account holder", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Current account balance", example = "1000.50")
    private BigDecimal balance;

    @Schema(description = "Timestamp when the account was created", example = "2026-08-21T10:00:00")
    private LocalDateTime createdAt;
}