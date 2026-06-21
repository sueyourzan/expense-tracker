package com.mj.expensetracker.repository;

import com.mj.expensetracker.entity.Transaction;
import com.mj.expensetracker.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByWalletIdAndUserId(Long walletId, Long userId);
    List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type AND t.transactionDate BETWEEN :start AND :end")
    BigDecimal getTotalAmountByTypeAndDateRange(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT t.category, COALESCE(SUM(t.amount), 0) as total FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = :type AND t.transactionDate BETWEEN :start AND :end " +
            "GROUP BY t.category")
    List<Object[]> getCategorySummary(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}