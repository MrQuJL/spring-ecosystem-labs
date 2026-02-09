-- 创建数据库
CREATE DATABASE IF NOT EXISTS `spring_labs` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `spring_labs`;

-- ----------------------------
-- 1. 客户表 (biz_customers)
-- ----------------------------
DROP TABLE IF EXISTS `biz_customers`;
CREATE TABLE `biz_customers` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '客户姓名',
  `balance` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-冻结, 1-正常',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_biz_customers_name` (`name`),
  INDEX `idx_biz_customers_status` (`status`),
  INDEX `idx_biz_customers_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户表';

-- 插入客户模拟数据
INSERT INTO `biz_customers` (`name`, `balance`, `status`, `created_at`, `updated_at`, `deleted`) VALUES
('客户A', 1000.00, 1, '2024-02-01 10:00:00', '2024-02-01 10:00:00', 0),
('客户B', 500.50, 1, '2024-02-02 11:30:00', '2024-02-02 11:30:00', 0),
('客户C', 0.00, 0, '2024-02-03 09:15:00', '2024-02-03 09:15:00', 0);


-- ----------------------------
-- 2. 产品表 (biz_products)
-- ----------------------------
DROP TABLE IF EXISTS `biz_products`;
CREATE TABLE `biz_products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `product_name` VARCHAR(100) NOT NULL COMMENT '产品名称',
  `price` DECIMAL(10,2) NOT NULL COMMENT '产品单价',
  `stock` INT NOT NULL COMMENT '库存数量',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_biz_products_status` (`status`),
  INDEX `idx_biz_products_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='产品表';

-- 插入产品模拟数据
INSERT INTO `biz_products` (`product_name`, `price`, `stock`, `status`, `created_at`, `updated_at`, `deleted`) VALUES
('高性能笔记本', 6999.00, 50, 1, '2024-01-15 08:00:00', '2024-01-15 08:00:00', 0),
('无线鼠标', 99.00, 200, 1, '2024-01-15 08:05:00', '2024-01-15 08:05:00', 0),
('机械键盘', 399.00, 100, 1, '2024-01-16 14:20:00', '2024-01-16 14:20:00', 0),
('高清显示器', 1299.00, 30, 0, '2024-01-17 16:45:00', '2024-01-17 16:45:00', 0);


-- ----------------------------
-- 3. 订单主表 (biz_orders)
-- ----------------------------
DROP TABLE IF EXISTS `biz_orders`;
CREATE TABLE `biz_orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
  `customer_id` BIGINT NOT NULL COMMENT '客户ID',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
  `status` TINYINT NOT NULL COMMENT '订单状态: 0-创建, 1-已支付, 2-已取消',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_biz_orders_order_no` (`order_no`),
  INDEX `idx_biz_orders_customer_id` (`customer_id`),
  INDEX `idx_biz_orders_status` (`status`),
  INDEX `idx_biz_orders_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单主表';

-- 插入订单模拟数据
INSERT INTO `biz_orders` (`order_no`, `customer_id`, `total_amount`, `status`, `created_at`, `updated_at`, `deleted`) VALUES
('ORD20240201001', 1, 7098.00, 1, '2024-02-01 10:05:00', '2024-02-01 10:05:00', 0),
('ORD20240202001', 2, 99.00, 0, '2024-02-02 11:35:00', '2024-02-02 11:35:00', 0);


-- ----------------------------
-- 4. 订单明细表 (biz_order_items)
-- ----------------------------
DROP TABLE IF EXISTS `biz_order_items`;
CREATE TABLE `biz_order_items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单明细ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `product_id` BIGINT NOT NULL COMMENT '产品ID',
  `product_name` VARCHAR(100) NOT NULL COMMENT '产品名称(冗余)',
  `price` DECIMAL(10,2) NOT NULL COMMENT '下单时产品单价',
  `quantity` INT NOT NULL COMMENT '购买数量',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_biz_order_items_order_id` (`order_id`),
  INDEX `idx_biz_order_items_product_id` (`product_id`),
  INDEX `idx_biz_order_items_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单明细表';

-- 插入订单明细模拟数据
INSERT INTO `biz_order_items` (`order_id`, `product_id`, `product_name`, `price`, `quantity`, `created_at`, `updated_at`, `deleted`) VALUES
(1, 1, '高性能笔记本', 6999.00, 1, '2024-02-01 10:05:00', '2024-02-01 10:05:00', 0),
(1, 2, '无线鼠标', 99.00, 1, '2024-02-01 10:05:00', '2024-02-01 10:05:00', 0),
(2, 2, '无线鼠标', 99.00, 1, '2024-02-02 11:35:00', '2024-02-02 11:35:00', 0);
