package com.example.bank.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    private String name;
    private String email;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}