package com.mj.expensetracker.service;

import com.mj.expensetracker.dto.WalletDTO;
import com.mj.expensetracker.entity.Wallet;
import com.mj.expensetracker.entity.User;
import com.mj.expensetracker.exception.ResourceNotFoundException;
import com.mj.expensetracker.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet createWallet(User user, WalletDTO walletDTO) {
        Wallet wallet = Wallet.builder()
                .name(walletDTO.getName())
                .description(walletDTO.getDescription())
                .balance(walletDTO.getInitialBalance())
                .currencyCode(walletDTO.getCurrencyCode())
                .user(user)
                .build();

        return walletRepository.save(wallet);
    }

    public List<Wallet> getUserWallets(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    public Wallet getWalletById(Long walletId, Long userId) {
        return walletRepository.findByIdAndUserId(walletId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("钱包不存在: " + walletId));
    }

    public Wallet updateWallet(Long walletId, Long userId, WalletDTO walletDTO) {
        Wallet wallet = getWalletById(walletId, userId);

        if (walletDTO.getName() != null) {
            wallet.setName(walletDTO.getName());
        }
        if (walletDTO.getDescription() != null) {
            wallet.setDescription(walletDTO.getDescription());
        }
        if (walletDTO.getCurrencyCode() != null) {
            wallet.setCurrencyCode(walletDTO.getCurrencyCode());
        }

        return walletRepository.save(wallet);
    }

    @Transactional
    public void deleteWallet(Long walletId, Long userId) {
        Wallet wallet = getWalletById(walletId, userId);
        walletRepository.delete(wallet);
    }

    public void updateWalletBalance(Long walletId, Long userId, BigDecimal amount) {
        Wallet wallet = getWalletById(walletId, userId);
        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
    }
}
