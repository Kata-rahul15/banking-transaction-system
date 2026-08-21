package com.example.bank.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Record of a banking transaction")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique transaction ID", example = "101")
    private Long id;

    @Schema(description = "Sender account ID (null for deposits)", example = "1")
    private Long senderAccountId; // null for deposit

    @Schema(description = "Receiver account ID (null for withdrawals)", example = "2")
    private Long receiverAccountId; // null for withdraw

    @Column(nullable = false, precision = 19, scale = 4)
    @Schema(description = "Transaction amount", example = "150.00")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of transaction: DEPOSIT, WITHDRAW, or TRANSFER")
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Status of transaction: SUCCESS or FAILED")
    private TransactionStatus status;

    @Schema(description = "Timestamp when the transaction took place", example = "2026-08-21T10:15:00")
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }
}