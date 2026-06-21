package com.mj.expensetracker.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class MoneyUtil {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    private MoneyUtil() {
        // 私有构造函数
    }

    /**
     * 格式化金额
     * @param amount 金额
     * @return 格式化后的字符串
     */
    public static String format(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return DECIMAL_FORMAT.format(amount);
    }

    /**
     * 格式化金额（带货币符号）
     * @param amount 金额
     * @param currencyCode 货币代码
     * @return 格式化后的字符串
     */
    public static String formatWithCurrency(BigDecimal amount, String currencyCode) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }

        try {
            Currency currency = Currency.getInstance(currencyCode);
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(currency);
            return format.format(amount);
        } catch (IllegalArgumentException e) {
            // 如果货币代码无效，使用默认格式
            return format(amount) + " " + currencyCode;
        }
    }

    /**
     * 四舍五入到两位小数
     * @param amount 金额
     * @return 四舍五入后的金额
     */
    public static BigDecimal round(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 计算百分比
     * @param part 部分
     * @param total 总数
     * @return 百分比
     */
    public static BigDecimal calculatePercentage(BigDecimal part, BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return part.multiply(new BigDecimal("100"))
                .divide(total, 2, RoundingMode.HALF_UP);
    }

    /**
     * 判断金额是否为零
     * @param amount 金额
     * @return 是否为零
     */
    public static boolean isZero(BigDecimal amount) {
        return amount == null || amount.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 判断金额是否为正数
     * @param amount 金额
     * @return 是否为正数
     */
    public static boolean isPositive(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 判断金额是否为负数
     * @param amount 金额
     * @return 是否为负数
     */
    public static boolean isNegative(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 安全地相加两个金额
     * @param a 金额1
     * @param b 金额2
     * @return 相加结果
     */
    public static BigDecimal safeAdd(BigDecimal a, BigDecimal b) {
        BigDecimal result = (a == null ? BigDecimal.ZERO : a)
                .add(b == null ? BigDecimal.ZERO : b);
        return round(result);
    }

    /**
     * 安全地相减两个金额
     * @param a 金额1
     * @param b 金额2
     * @return 相减结果
     */
    public static BigDecimal safeSubtract(BigDecimal a, BigDecimal b) {
        BigDecimal result = (a == null ? BigDecimal.ZERO : a)
                .subtract(b == null ? BigDecimal.ZERO : b);
        return round(result);
    }

    /**
     * 转换字符串为BigDecimal
     * @param amountStr 金额字符串
     * @return BigDecimal对象
     */
    public static BigDecimal parse(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        try {
            // 移除逗号和其他非数字字符（除了点和负号）
            String cleanStr = amountStr.replaceAll("[^0-9.-]", "");
            return new BigDecimal(cleanStr);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}