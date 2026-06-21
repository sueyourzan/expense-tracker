package com.mj.expensetracker.dto;

import com.mj.expensetracker.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    @NotNull(message = "交易类型不能为空")
    private TransactionType type;

    @NotBlank(message = "分类不能为空")
    private String category;

    private String description;

    @NotNull(message = "钱包ID不能为空")
    private Long walletId;

    private LocalDateTime transactionDate;
}
