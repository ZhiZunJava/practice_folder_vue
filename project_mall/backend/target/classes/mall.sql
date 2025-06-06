-- 创建数据库
CREATE DATABASE IF NOT EXISTS mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mall;

-- 管理员表
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用 1:正常)',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除 1:已删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    gender TINYINT COMMENT '性别(0:未知 1:男 2:女)',
    birth_date DATE COMMENT '生日',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用 1:正常)',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除 1:已删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品分类表
CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    icon VARCHAR(255) COMMENT '分类图标',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID(0为顶级分类)',
    level TINYINT DEFAULT 1 COMMENT '分类级别',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用 1:正常)',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除 1:已删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    subtitle VARCHAR(200) COMMENT '副标题',
    main_image VARCHAR(500) COMMENT '主图',
    images TEXT COMMENT '图片集合',
    detail TEXT COMMENT '商品详情',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT DEFAULT 0 COMMENT '库存',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    status TINYINT DEFAULT 1 COMMENT '状态(0:下架 1:上架)',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除 1:已删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 购物车表
CREATE TABLE IF NOT EXISTS cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT DEFAULT 1 COMMENT '数量',
    checked TINYINT DEFAULT 1 COMMENT '是否选中(0:未选中 1:选中)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(100) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_price DECIMAL(10,2) NOT NULL COMMENT '总金额',
    status TINYINT DEFAULT 1 COMMENT '订单状态(1:待支付 2:已支付 3:已发货 4:已完成 5:已取消)',
    receiver_name VARCHAR(50) COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) COMMENT '收货人电话',
    receiver_address VARCHAR(500) COMMENT '收货地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单商品表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(500) COMMENT '商品图片',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    quantity INT NOT NULL COMMENT '数量',
    total_price DECIMAL(10,2) NOT NULL COMMENT '小计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';

-- 插入初始数据

-- 插入管理员
INSERT INTO admin (username, password, real_name, email) VALUES 
('admin', '123456', '系统管理员', 'admin@mall.com');

-- 插入测试用户
INSERT INTO user (username, password, nickname, email) VALUES 
('user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8ioctKi9Z4NbEOeJy9yXd8rKnrw2y', '测试用户', 'user@mall.com');

-- 插入商品分类
INSERT INTO category (name, icon, parent_id, level, sort_order) VALUES 
('数码电器', '📱', 0, 1, 1),
('服装鞋包', '👕', 0, 1, 2),
('家居生活', '🏠', 0, 1, 3),
('美妆护肤', '💄', 0, 1, 4),
('运动户外', '⚽', 0, 1, 5),
('图书音像', '📚', 0, 1, 6);

-- 插入二级分类
INSERT INTO category (name, icon, parent_id, level, sort_order) VALUES 
('手机通讯', '📱', 1, 2, 1),
('电脑办公', '💻', 1, 2, 2),
('家用电器', '🔌', 1, 2, 3),
('男装', '👔', 2, 2, 1),
('女装', '👗', 2, 2, 2),
('鞋靴', '👠', 2, 2, 3),
('厨房用品', '🍳', 3, 2, 1),
('家纺用品', '🛏️', 3, 2, 2),
('家具', '🪑', 3, 2, 3);

-- 插入示例商品
INSERT INTO product (name, subtitle, main_image, price, stock, category_id) VALUES 
('iPhone 15 Pro', '最新款苹果手机，性能强劲', '/images/products/iphone15.jpg', 7999.00, 100, 7),
('MacBook Pro', '专业级笔记本电脑', '/images/products/macbook.jpg', 12999.00, 50, 8),
('Nike运动鞋', '舒适透气运动鞋', '/images/products/nike.jpg', 599.00, 200, 12),
('时尚连衣裙', '优雅气质连衣裙', '/images/products/dress.jpg', 299.00, 150, 11),
('智能电饭煲', '多功能智能电饭煲', '/images/products/cooker.jpg', 399.00, 80, 13);

-- 创建索引
CREATE INDEX idx_user_username ON user(username);
CREATE INDEX idx_admin_username ON admin(username);
CREATE INDEX idx_category_parent_id ON category(parent_id);
CREATE INDEX idx_product_category_id ON product(category_id);
CREATE INDEX idx_cart_user_id ON cart(user_id);
CREATE INDEX idx_order_user_id ON `order`(user_id);
CREATE INDEX idx_order_item_order_id ON order_item(order_id); 