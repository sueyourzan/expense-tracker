package com.mj.expensetracker.service;

import com.mj.expensetracker.dto.TransactionDTO;
import com.mj.expensetracker.entity.Transaction;
import com.mj.expensetracker.entity.TransactionType;
import com.mj.expensetracker.entity.User;
import com.mj.expensetracker.entity.Wallet;
import com.mj.expensetracker.exception.InsufficientBalanceException;
import com.mj.expensetracker.exception.ResourceNotFoundException;
import com.mj.expensetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    @Transactional
    public Transaction createTransaction(User user, TransactionDTO transactionDTO) {
        Wallet wallet = walletService.getWalletById(transactionDTO.getWalletId(), user.getId());

        if (transactionDTO.getType() == TransactionType.EXPENSE) {
            if (wallet.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
                throw new InsufficientBalanceException("钱包余额不足");
            }
        }

        Transaction transaction = Transaction.builder()
                .amount(transactionDTO.getAmount())
                .type(transactionDTO.getType())
                .category(transactionDTO.getCategory())
                .description(transactionDTO.getDescription())
                .transactionDate(transactionDTO.getTransactionDate() != null ?
                        transactionDTO.getTransactionDate() : LocalDateTime.now())
                .wallet(wallet)
                .user(user)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        // 更新钱包余额
        BigDecimal amount = transactionDTO.getType() == TransactionType.INCOME ?
                transactionDTO.getAmount() : transactionDTO.getAmount().negate();
        walletService.updateWalletBalance(wallet.getId(), user.getId(), amount);

        return savedTransaction;
    }

    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getWalletTransactions(Long walletId, Long userId) {
        return transactionRepository.findByWalletIdAndUserId(walletId, userId);
    }

    public Transaction getTransactionById(Long transactionId, Long userId) {
        return transactionRepository.findById(transactionId)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("交易记录不存在: " + transactionId));
    }

    @Transactional
    public void deleteTransaction(Long transactionId, Long userId) {
        Transaction transaction = getTransactionById(transactionId, userId);

        // 恢复钱包余额
        BigDecimal amount = transaction.getType() == TransactionType.INCOME ?
                transaction.getAmount().negate() : transaction.getAmount();
        walletService.updateWalletBalance(transaction.getWallet().getId(), userId, amount);

        transactionRepository.delete(transaction);
    }

    public List<Transaction> getTransactionsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, start, end);
    }

    public BigDecimal getTotalIncome(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return transactionRepository.getTotalAmountByTypeAndDateRange(userId, TransactionType.INCOME, start, end);
    }

    public BigDecimal getTotalExpense(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return transactionRepository.getTotalAmountByTypeAndDateRange(userId, TransactionType.EXPENSE, start, end);
    }

    public List<Object[]> getCategorySummary(Long userId, TransactionType type, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return transactionRepository.getCategorySummary(userId, type, start, end);
    }
}
