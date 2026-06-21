package com.mj.expensetracker.controller;

import com.mj.expensetracker.dto.TransactionDTO;
import com.mj.expensetracker.entity.Transaction;
import com.mj.expensetracker.entity.TransactionType;
import com.mj.expensetracker.entity.User;
import com.mj.expensetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TransactionDTO transactionDTO) {
        Transaction transaction = transactionService.createTransaction(user, transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Long walletId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            List<Transaction> byDate = transactionService.getTransactionsByDateRange(user.getId(), startDate, endDate);
            if (walletId != null) {
                byDate = byDate.stream()
                        .filter(t -> t.getWallet().getId().equals(walletId))
                        .collect(Collectors.toList());
            }
            return ResponseEntity.ok(byDate);
        }
        if (walletId != null) {
            return ResponseEntity.ok(transactionService.getWalletTransactions(walletId, user.getId()));
        }
        return ResponseEntity.ok(transactionService.getUserTransactions(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id, user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        transactionService.deleteTransaction(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats(
            @AuthenticationPrincipal User user,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        BigDecimal totalIncome = transactionService.getTotalIncome(user.getId(), startDate, endDate);
        BigDecimal totalExpense = transactionService.getTotalExpense(user.getId(), startDate, endDate);
        List<Object[]> incomeByCategory = transactionService.getCategorySummary(user.getId(), TransactionType.INCOME, startDate, endDate);
        List<Object[]> expenseByCategory = transactionService.getCategorySummary(user.getId(), TransactionType.EXPENSE, startDate, endDate);

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalIncome", totalIncome != null ? totalIncome : BigDecimal.ZERO);
        stats.put("totalExpense", totalExpense != null ? totalExpense : BigDecimal.ZERO);
        stats.put("balance", (totalIncome != null ? totalIncome : BigDecimal.ZERO)
                .subtract(totalExpense != null ? totalExpense : BigDecimal.ZERO));
        stats.put("incomeByCategory", incomeByCategory.stream()
                .map(row -> Map.of("category", row[0], "total", row[1]))
                .collect(Collectors.toList()));
        stats.put("expenseByCategory", expenseByCategory.stream()
                .map(row -> Map.of("category", row[0], "total", row[1]))
                .collect(Collectors.toList()));
        return ResponseEntity.ok(stats);
    }
}
