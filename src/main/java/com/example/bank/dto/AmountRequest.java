package com.example.bank.dto;
import lombok.*;

@Data
public class AmountRequest {
    private String amount;

    public String getAmount() {
        return amount;
    }
}