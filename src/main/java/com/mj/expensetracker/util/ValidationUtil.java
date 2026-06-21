package com.mj.expensetracker.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^1[3-9]\\d{9}$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9_]{2,20}$");

    private ValidationUtil() {
        // 私有构造函数
    }

    /**
     * 验证邮箱格式
     * @param email 邮箱
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式
     * @param phone 手机号
     * @return 是否有效
     */
    public static boolean isValidPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证用户名格式
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean isValidUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * 验证密码强度
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValidPassword(String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }

        // 密码长度至少6位
        if (password.length() < 6) {
            return false;
        }

        // 检查是否包含数字
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // 检查是否包含字母
        if (!password.matches(".*[a-zA-Z].*")) {
            return false;
        }

        return true;
    }

    /**
     * 验证金额是否有效
     * @param amount 金额
     * @return 是否有效
     */
    public static boolean isValidAmount(BigDecimal amount) {
        if (amount == null) {
            return false;
        }

        // 金额不能为负数
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        // 金额不能过大
        if (amount.compareTo(new BigDecimal("1000000000")) > 0) {
            return false;
        }

        // 小数位数不能超过2位
        if (amount.scale() > 2) {
            return false;
        }

        return true;
    }

    /**
     * 验证字符串长度
     * @param str 字符串
     * @param min 最小长度
     * @param max 最大长度
     * @return 是否有效
     */
    public static boolean isValidLength(String str, int min, int max) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= min && length <= max;
    }

    /**
     * 验证数字范围
     * @param number 数字
     * @param min 最小值
     * @param max 最大值
     * @return 是否有效
     */
    public static boolean isValidRange(int number, int min, int max) {
        return number >= min && number <= max;
    }

    /**
     * 验证是否为正整数
     * @param number 数字
     * @return 是否有效
     */
    public static boolean isValidPositiveInteger(Integer number) {
        return number != null && number > 0;
    }

    /**
     * 验证对象是否为空
     * @param obj 对象
     * @param fieldName 字段名
     * @throws IllegalArgumentException 如果对象为空
     */
    public static void requireNonNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException(fieldName + "不能为空");
        }
    }

    /**
     * 验证字符串是否为空
     * @param str 字符串
     * @param fieldName 字段名
     * @throws IllegalArgumentException 如果字符串为空
     */
    public static void requireNonEmpty(String str, String fieldName) {
        if (!StringUtils.hasText(str)) {
            throw new IllegalArgumentException(fieldName + "不能为空");
        }
    }
}
