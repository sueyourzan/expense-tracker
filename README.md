<p align="center">
  <h1 align="center">💰 Expense Tracker</h1>
  <p align="center">全栈个人财务管理应用 — Spring Boot 3 + Vue 3 前后端分离</p>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?logo=openjdk" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen?logo=springboot" alt="Spring Boot 3">
  <img src="https://img.shields.io/badge/Vue-3.x-4FC08D?logo=vuedotjs" alt="Vue 3">
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql" alt="MySQL 8">
  <img src="https://img.shields.io/badge/Docker-ready-2496ED?logo=docker" alt="Docker">
  <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="License">
</p>

---

## ✨ 功能特性

### 后端 API
| 模块     | 功能                                         |
| -------- | -------------------------------------------- |
| 🔐 认证  | 用户注册 / 登录 / JWT Token + Refresh Token |
| 👤 用户  | 个人信息查询与修改                           |
| 💳 钱包  | 创建、查看、更新、删除，余额自动核算         |
| 💸 交易  | 收入 / 支出记录，按分类和时间筛选            |
| 📂 分类  | 收支分类管理                                 |
| 📊 统计  | 按时间范围汇总报表                           |

### 前端 SPA (Vue 3)
- 登录 / 注册页面
- 仪表盘概览
- 钱包管理（增删改查）
- 收支记录（筛选、新增、编辑）
- 个人信息管理
- Axios 拦截器 + Pinia 状态管理 + Vue Router

### 通用
- ✅ JWT 无状态认证（Access + Refresh Token）
- ✅ Swagger / OpenAPI 文档
- ✅ 统一异常处理与响应格式
- ✅ Docker Compose 一键部署
- ✅ CORS 跨域支持

---

## 🛠 技术栈

| 层级       | 技术                              |
| ---------- | --------------------------------- |
| 后端框架   | Spring Boot 3.1.5                 |
| 安全       | Spring Security 6 + JWT           |
| 数据库     | MySQL 8.0                         |
| ORM        | Spring Data JPA (Hibernate)       |
| API 文档   | SpringDoc OpenAPI 3               |
| 前端框架   | Vue 3 + Vue Router + Pinia        |
| HTTP 客户端| Axios                             |
| 构建工具   | Maven                             |
| 容器化     | Docker & Docker Compose            |
| Java 版本  | 17                                |

---

## 📁 项目结构

```
expense-tracker/
├── src/main/java/com/mj/expensetracker/
│   ├── config/           # Security、JWT、CORS、OpenAPI 配置
│   ├── controller/       # REST 控制器 (Auth, User, Wallet, Transaction, Category)
│   ├── dto/              # 数据传输对象
│   ├── entity/           # JPA 实体及枚举
│   ├── exception/        # 全局异常处理
│   ├── repository/       # Spring Data JPA 仓库
│   ├── service/          # 业务逻辑层
│   └── util/             # JWT、加解密、日期等工具类
├── src/main/resources/
│   ├── application.yml           # 公共配置
│   ├── application-dev.yml       # 开发环境
│   ├── application-prod.yml      # 生产环境
│   └── sql/                      # 数据库初始化脚本
├── expense-tracker-frontend/     # Vue 3 前端项目
│   └── src/
│       ├── api/          # API 调用封装
│       ├── components/   # 通用组件
│       ├── router/       # 路由配置
│       ├── stores/       # Pinia 状态管理
│       └── views/        # 页面组件
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

## 🚀 快速开始

### 前置条件

- **JDK 17+**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Node.js 16+**（仅前端开发需要）
- **Docker 20.10+**（可选）

### 1. 克隆项目

```bash
git clone https://github.com/sueyourzan/expense-tracker.git
cd expense-tracker
```

### 2. 配置数据库

创建 MySQL 数据库：

```sql
CREATE DATABASE expense_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `src/main/resources/application-dev.yml` 中的数据库连接信息。

### 3. 启动后端

```bash
# 开发环境（dev profile）
mvnw spring-boot:run

# 或打包运行
mvnw clean package -DskipTests
java -jar target/expense-tracker-0.0.1-SNAPSHOT.jar
```

后端启动后访问 http://localhost:8080

### 4. 启动前端（可选）

```bash
cd expense-tracker-frontend
npm install
npm run serve
```

前端开发服务器运行在 http://localhost:8081

### 5. Docker 部署

```bash
# 一键启动（MySQL + 后端）
docker-compose up -d

# 查看日志
docker-compose logs -f app

# 停止
docker-compose down
```

---

## 📖 API 文档

启动应用后访问：

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### 主要接口

| 方法     | 路径                      | 说明         | 认证 |
| -------- | ------------------------- | ------------ | ---- |
| `POST`   | `/api/auth/register`      | 用户注册     | ❌   |
| `POST`   | `/api/auth/login`         | 用户登录     | ❌   |
| `POST`   | `/api/auth/refresh`       | 刷新 Token   | ❌   |
| `GET`    | `/api/users/me`           | 当前用户信息 | ✅   |
| `PUT`    | `/api/users/me`           | 更新用户信息 | ✅   |
| `GET`    | `/api/wallets`            | 钱包列表     | ✅   |
| `POST`   | `/api/wallets`            | 创建钱包     | ✅   |
| `PUT`    | `/api/wallets/{id}`       | 更新钱包     | ✅   |
| `DELETE` | `/api/wallets/{id}`       | 删除钱包     | ✅   |
| `GET`    | `/api/transactions`       | 交易列表     | ✅   |
| `POST`   | `/api/transactions`       | 创建交易     | ✅   |
| `PUT`    | `/api/transactions/{id}`  | 更新交易     | ✅   |
| `DELETE` | `/api/transactions/{id}`  | 删除交易     | ✅   |
| `GET`    | `/api/categories`         | 分类列表     | ✅   |

---

## ⚙️ 环境变量

| 变量                | 说明           | 默认值      |
| ------------------- | -------------- | ----------- |
| `DATABASE_URL`      | 数据库连接 URL | `jdbc:mysql://localhost:3306/expense_tracker` |
| `DATABASE_USERNAME` | 数据库用户名   | `root`      |
| `DATABASE_PASSWORD` | 数据库密码     | —           |
| `jwt.secret-key`    | JWT 签名密钥   | (应用内置)  |
| `jwt.expiration`    | Token 有效期   | `86400000` (24h) |
| `jwt.refresh-expiration` | Refresh Token 有效期 | `604800000` (7d) |
