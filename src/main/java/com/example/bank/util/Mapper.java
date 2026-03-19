package com.example.bank.util;

import com.example.bank.dto.AccountDto;
import com.example.bank.entity.Account;

public class Mapper {
    public static AccountDto toDto(Account a) {
        return new AccountDto(a.getId(), a.getName(), a.getEmail(), a.getBalance(), a.getCreatedAt());
    }
}