package com.example.bank.dto;
import lombok.*;

@Data
public class CreateAccountRequest {
    private String name;
    private String email;
    private String initialBalance; // send as string to avoid JSON float issues
}