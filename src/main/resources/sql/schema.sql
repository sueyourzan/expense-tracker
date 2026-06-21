-- 创建数据库
CREATE DATABASE IF NOT EXISTS expense_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE expense_tracker;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR(100) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     username VARCHAR(50) NOT NULL,
                                     phone VARCHAR(20),
                                     role ENUM('USER', 'ADMIN') DEFAULT 'USER',
                                     enabled BOOLEAN DEFAULT TRUE,
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     INDEX idx_email (email),
                                     INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 钱包表
CREATE TABLE IF NOT EXISTS wallets (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL,
                                       description TEXT,
                                       balance DECIMAL(15, 2) DEFAULT 0.00,
                                       currency_code VARCHAR(10) DEFAULT 'CNY',
                                       user_id BIGINT NOT NULL,
                                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                       INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 交易记录表
CREATE TABLE IF NOT EXISTS transactions (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            amount DECIMAL(15, 2) NOT NULL,
                                            type ENUM('INCOME', 'EXPENSE') NOT NULL,
                                            category VARCHAR(50) NOT NULL,
                                            description TEXT,
                                            transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                            wallet_id BIGINT NOT NULL,
                                            user_id BIGINT NOT NULL,
                                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                            FOREIGN KEY (wallet_id) REFERENCES wallets(id) ON DELETE CASCADE,
                                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                            INDEX idx_user_id (user_id),
                                            INDEX idx_wallet_id (wallet_id),
                                            INDEX idx_transaction_date (transaction_date),
                                            INDEX idx_type (type),
                                            INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 分类表
CREATE TABLE IF NOT EXISTS categories (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(50) NOT NULL,
                                          type ENUM('INCOME', 'EXPENSE') NOT NULL,
                                          user_id BIGINT,
                                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                          UNIQUE KEY uk_name_user (name, user_id),
                                          INDEX idx_user_id (user_id),
                                          INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;