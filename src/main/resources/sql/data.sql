-- 插入默认分类
INSERT IGNORE INTO categories (name, type, user_id) VALUES
                                                        ('工资', 'INCOME', NULL),
                                                        ('奖金', 'INCOME', NULL),
                                                        ('投资', 'INCOME', NULL),
                                                        ('其他收入', 'INCOME', NULL),
                                                        ('餐饮', 'EXPENSE', NULL),
                                                        ('交通', 'EXPENSE', NULL),
                                                        ('购物', 'EXPENSE', NULL),
                                                        ('娱乐', 'EXPENSE', NULL),
                                                        ('住房', 'EXPENSE', NULL),
                                                        ('医疗', 'EXPENSE', NULL),
                                                        ('教育', 'EXPENSE', NULL),
                                                        ('其他支出', 'EXPENSE', NULL);

-- 创建默认管理员用户 (密码: admin123)
INSERT IGNORE INTO users (email, password, username, role, enabled) VALUES
    ('admin@wallet.com', '$2a$10$YourEncodedPasswordHere', '管理员', 'ADMIN', true);