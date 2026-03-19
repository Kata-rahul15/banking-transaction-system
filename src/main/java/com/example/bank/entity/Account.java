package com.example.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    private LocalDateTime createdAt;

    @Version
    private Long version; // keeps optimistic lock info if you want to use it

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (balance == null) balance = BigDecimal.ZERO;
    }
}