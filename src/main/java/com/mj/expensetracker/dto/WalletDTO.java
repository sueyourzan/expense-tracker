package com.mj.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {

    @NotBlank(message = "钱包名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "初始余额不能为空")
    private BigDecimal initialBalance;

    private String currencyCode;
}
