package com.example.bank.controller;

import com.example.bank.dto.*;
import com.example.bank.entity.TransactionRecord;
import com.example.bank.service.AccountService;
import com.example.bank.util.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService svc;

    public AccountController(AccountService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<AccountDto> create(@RequestBody CreateAccountRequest req) {
        return ResponseEntity.ok(svc.createAccount(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(svc.getAccount(id));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long id, @RequestBody AmountRequest req) {
        svc.deposit(id, new BigDecimal(req.getAmount()));
        return ResponseEntity.ok(Map.of("status","OK"));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long id, @RequestBody AmountRequest req) {
        svc.withdraw(id, new BigDecimal(req.getAmount()));
        return ResponseEntity.ok(Map.of("status","OK"));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest req) {
        svc.transfer(req.getFromAccountId(), req.getToAccountId(), new BigDecimal(req.getAmount()));
        return ResponseEntity.ok(Map.of("status","OK"));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionRecord>> transactions(@PathVariable Long id) {
        List<TransactionRecord> txs = svc.getTransactionsForAccount(id);
        return ResponseEntity.ok(txs);
    }
}