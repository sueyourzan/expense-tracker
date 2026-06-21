package com.mj.expensetracker.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtil {

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final int AES_KEY_SIZE = 128;

    private CryptoUtil() {
        // 私有构造函数
    }

    /**
     * 生成AES密钥
     * @return Base64编码的密钥
     */
    public static String generateAesKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGen.init(AES_KEY_SIZE, new SecureRandom());
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }

    /**
     * AES加密
     * @param data 待加密数据
     * @param key Base64编码的密钥
     * @return Base64编码的加密结果
     */
    public static String aesEncrypt(String data, String key) {
        try {
            Key secretKey = new SecretKeySpec(
                    Base64.getDecoder().decode(key),
                    AES_ALGORITHM
            );

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encrypted = cipher.doFinal(
                    data.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * AES解密
     * @param encryptedData Base64编码的加密数据
     * @param key Base64编码的密钥
     * @return 解密后的字符串
     */
    public static String aesDecrypt(String encryptedData, String key) {
        try {
            Key secretKey = new SecretKeySpec(
                    Base64.getDecoder().decode(key),
                    AES_ALGORITHM
            );

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decrypted = cipher.doFinal(
                    Base64.getDecoder().decode(encryptedData)
            );

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * Base64编码
     * @param data 原始数据
     * @return Base64编码结果
     */
    public static String base64Encode(String data) {
        return Base64.getEncoder().encodeToString(
                data.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Base64解码
     * @param encodedData Base64编码数据
     * @return 解码结果
     */
    public static String base64Decode(String encodedData) {
        byte[] decoded = Base64.getDecoder().decode(encodedData);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    /**
     * 生成随机字符串
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    /**
     * 生成验证码（6位数字）
     * @return 6位数字验证码
     */
    public static String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * 简单的字符串混淆（用于日志脱敏等场景）
     * @param str 原始字符串
     * @return 混淆后的字符串
     */
    public static String obscureString(String str) {
        if (str == null || str.length() <= 2) {
            return "***";
        }

        int visibleLength = Math.min(4, str.length() / 3);
        String start = str.substring(0, visibleLength);
        String end = str.substring(str.length() - visibleLength);

        StringBuilder obscured = new StringBuilder(start);
        for (int i = 0; i < str.length() - visibleLength * 2; i++) {
            obscured.append('*');
        }
        obscured.append(end);

        return obscured.toString();
    }
}
