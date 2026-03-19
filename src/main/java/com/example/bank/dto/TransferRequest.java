package com.example.bank.dto;
import lombok.*;

@Data
public class TransferRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private String amount;


}