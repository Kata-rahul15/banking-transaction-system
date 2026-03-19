package com.example.bank.repository;

import com.example.bank.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // For safe updates: pessimistic write lock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account> findByIdForUpdate(@Param("id") Long id);

    Optional<Account> findByEmail(String email);
}