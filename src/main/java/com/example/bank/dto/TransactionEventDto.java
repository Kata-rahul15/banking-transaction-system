package com.example.bank.dto;
import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEventDto {
    private Long transactionId;
    private Long sender;
    private Long receiver;
    private BigDecimal amount;
    private String status;
    private String type;


}