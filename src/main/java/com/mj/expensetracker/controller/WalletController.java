package com.mj.expensetracker.controller;

import com.mj.expensetracker.dto.WalletDTO;
import com.mj.expensetracker.entity.User;
import com.mj.expensetracker.entity.Wallet;
import com.mj.expensetracker.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<Wallet> createWallet(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody WalletDTO walletDTO) {
        Wallet wallet = walletService.createWallet(user, walletDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    @GetMapping
    public ResponseEntity<List<Wallet>> getWallets(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(walletService.getUserWallets(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWallet(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return ResponseEntity.ok(walletService.getWalletById(id, user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wallet> updateWallet(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody WalletDTO walletDTO) {
        return ResponseEntity.ok(walletService.updateWallet(id, user.getId(), walletDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        walletService.deleteWallet(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
