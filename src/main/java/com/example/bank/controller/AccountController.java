package com.example.bank.controller;

import com.example.bank.dto.*;
import com.example.bank.entity.TransactionRecord;
import com.example.bank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Account management operations")
public class AccountController {

    private final AccountService svc;

    public AccountController(AccountService svc) {
        this.svc = svc;
    }

    @PostMapping
    @Operation(summary = "Create a new bank account", description = "Creates a new bank account with initial holder details and balance.", tags = { "Accounts" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account successfully created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content)
    })
    public ResponseEntity<AccountDto> create(@RequestBody CreateAccountRequest req) {
        return ResponseEntity.ok(svc.createAccount(req));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account details", description = "Retrieves account information for the given account ID.", tags = { "Accounts" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account details retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    public ResponseEntity<AccountDto> get(
            @Parameter(description = "ID of the account to retrieve", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(svc.getAccount(id));
    }

    @PostMapping("/{id}/deposit")
    @Operation(summary = "Deposit funds", description = "Deposits specified amount into the account with the given ID.", tags = { "Transactions" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deposit completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid deposit amount"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<?> deposit(
            @Parameter(description = "ID of the account to deposit funds into", required = true) @PathVariable Long id,
            @RequestBody AmountRequest req) {
        svc.deposit(id, new BigDecimal(req.getAmount()));
        return ResponseEntity.ok(Map.of("status","OK"));
    }

    @PostMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw funds", description = "Withdraws specified amount from the account with the given ID.", tags = { "Transactions" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Withdrawal completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid withdrawal amount or insufficient balance"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<?> withdraw(
            @Parameter(description = "ID of the account to withdraw funds from", required = true) @PathVariable Long id,
            @RequestBody AmountRequest req) {
        svc.withdraw(id, new BigDecimal(req.getAmount()));
        return ResponseEntity.ok(Map.of("status","OK"));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer funds", description = "Transfers specified amount from one bank account to another.", tags = { "Transfers" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid transfer request or insufficient balance"),
        @ApiResponse(responseCode = "404", description = "Source or destination account not found")
    })
    public ResponseEntity<?> transfer(@RequestBody TransferRequest req) {
        svc.transfer(req.getFromAccountId(), req.getToAccountId(), new BigDecimal(req.getAmount()));
        return ResponseEntity.ok(Map.of("status","OK"));
    }

    @GetMapping("/{id}/transactions")
    @Operation(summary = "Get account transaction history", description = "Retrieves a list of transaction records associated with the specified account.", tags = { "Transactions" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction records retrieved successfully",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TransactionRecord.class)))),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<List<TransactionRecord>> transactions(
            @Parameter(description = "ID of the account to fetch transaction history for", required = true) @PathVariable Long id) {
        List<TransactionRecord> txs = svc.getTransactionsForAccount(id);
        return ResponseEntity.ok(txs);
    }
}