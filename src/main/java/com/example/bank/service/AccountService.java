package com.example.bank.service;

import com.example.bank.dto.*;
import com.example.bank.entity.*;
import com.example.bank.exception.*;
import com.example.bank.repository.*;
import com.example.bank.util.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;
    private final EventPublisher eventPublisher;

    public AccountService(AccountRepository accountRepo,
                          TransactionRepository txRepo,
                          EventPublisher eventPublisher) {
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
        this.eventPublisher = eventPublisher;
    }

    public AccountDto createAccount(CreateAccountRequest req) {
        BigDecimal initial = req.getInitialBalance() == null ? BigDecimal.ZERO :
                new BigDecimal(req.getInitialBalance());
        Account acc = Account.builder()
                .name(req.getName())
                .email(req.getEmail())
                .balance(initial)
                .build();
        Account saved = accountRepo.save(acc);
        return Mapper.toDto(saved);
    }

    public AccountDto getAccount(Long id) {
        Account a = accountRepo.findById(id).orElseThrow(() -> new AccountNotFoundException("Account " + id + " not found"));
        return Mapper.toDto(a);
    }

    @Transactional
    public void deposit(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be positive");
        Account acc = accountRepo.findByIdForUpdate(accountId).orElseThrow(() -> new AccountNotFoundException("Account " + accountId + " not found"));
        acc.setBalance(acc.getBalance().add(amount));
        accountRepo.save(acc);

        TransactionRecord tx = TransactionRecord.builder()
                .receiverAccountId(accountId)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.SUCCESS)
                .build();
        TransactionRecord saved = txRepo.save(tx);

        // publish after commit
        registerAfterCommitPublish(saved);
    }

    @Transactional
    public void withdraw(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be positive");
        Account acc = accountRepo.findByIdForUpdate(accountId).orElseThrow(() -> new AccountNotFoundException("Account " + accountId + " not found"));
        if (acc.getBalance().compareTo(amount) < 0) throw new InsufficientBalanceException("Insufficient balance");
        acc.setBalance(acc.getBalance().subtract(amount));
        accountRepo.save(acc);

        TransactionRecord tx = TransactionRecord.builder()
                .senderAccountId(accountId)
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .status(TransactionStatus.SUCCESS)
                .build();
        TransactionRecord saved = txRepo.save(tx);
        registerAfterCommitPublish(saved);
    }

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        if (fromId.equals(toId)) throw new IllegalArgumentException("Sender and receiver must differ");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be positive");

        // lock accounts in id order to avoid deadlocks
        Long first = Math.min(fromId, toId);
        Long second = Math.max(fromId, toId);

        Account a1 = accountRepo.findByIdForUpdate(first).orElseThrow(() -> new AccountNotFoundException("Account " + first + " not found"));
        Account a2 = accountRepo.findByIdForUpdate(second).orElseThrow(() -> new AccountNotFoundException("Account " + second + " not found"));

        Account from = (a1.getId().equals(fromId)) ? a1 : a2;
        Account to = (a1.getId().equals(fromId)) ? a2 : a1;

        if (from.getBalance().compareTo(amount) < 0) throw new InsufficientBalanceException("Insufficient balance for transfer");

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepo.save(from);
        accountRepo.save(to);

        TransactionRecord tx = TransactionRecord.builder()
                .senderAccountId(fromId)
                .receiverAccountId(toId)
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .build();

        TransactionRecord saved = txRepo.save(tx);
        registerAfterCommitPublish(saved);
    }

    public List<TransactionRecord> getTransactionsForAccount(Long accountId) {
        return txRepo.findBySenderAccountIdOrReceiverAccountIdOrderByTimestampDesc(accountId, accountId);
    }

    private void registerAfterCommitPublish(TransactionRecord tx) {
        // Register a synchronization to publish the event only after DB commit
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                TransactionEventDto event = new TransactionEventDto(
                        tx.getId(),
                        tx.getSenderAccountId(),
                        tx.getReceiverAccountId(),
                        tx.getAmount(),
                        tx.getStatus().name(),
                        tx.getType().name()
                );
                eventPublisher.publish(event);
            }
        });
    }
}