-- 电商系统数据库脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mall;

-- 管理员表
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-保密，1-男，2-女',
    birthday DATE COMMENT '生日',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID，0表示一级分类',
    level TINYINT DEFAULT 1 COMMENT '分类级别',
    sort INT DEFAULT 0 COMMENT '排序',
    icon VARCHAR(255) COMMENT '分类图标',
    image VARCHAR(255) COMMENT '分类图片',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    subtitle VARCHAR(200) COMMENT '商品副标题',
    main_image VARCHAR(500) COMMENT '商品主图',
    sub_images TEXT COMMENT '商品子图，多张图片用逗号分隔',
    detail TEXT COMMENT '商品详情',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    stock INT DEFAULT 0 COMMENT '商品库存',
    category_id BIGINT NOT NULL COMMENT '商品分类ID',
    status TINYINT DEFAULT 1 COMMENT '商品状态：0-下架，1-上架',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 购物车表
CREATE TABLE IF NOT EXISTS cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '商品数量',
    checked TINYINT DEFAULT 1 COMMENT '是否选中：0-未选中，1-已选中',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_product (user_id, product_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 订单表
CREATE TABLE IF NOT EXISTS order_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiver_address VARCHAR(200) NOT NULL COMMENT '收货地址',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    payment_amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    status TINYINT DEFAULT 1 COMMENT '订单状态：0-已取消，1-待付款，2-已付款，3-已发货，4-已完成',
    payment_type TINYINT COMMENT '支付方式：1-支付宝，2-微信',
    payment_time DATETIME COMMENT '支付时间',
    send_time DATETIME COMMENT '发货时间',
    end_time DATETIME COMMENT '完成时间',
    close_time DATETIME COMMENT '关闭时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单项表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单项ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(500) COMMENT '商品主图',
    current_unit_price DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    quantity INT NOT NULL COMMENT '商品数量',
    total_price DECIMAL(10,2) NOT NULL COMMENT '商品总价',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_id (order_id),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

-- 插入管理员初始数据
INSERT INTO admin (username, password, nickname, email, status) VALUES 
('admin', '123456', '系统管理员', 'admin@mall.com', 1);

-- 插入用户初始数据
INSERT INTO user (username, password, nickname, email, phone, gender, status) VALUES 
('user001', '123456', '张三', 'zhangsan@example.com', '13800138001', 1, 1),
('user002', '123456', '李四', 'lisi@example.com', '13800138002', 2, 1);

-- 插入分类初始数据
INSERT INTO category (name, parent_id, level, sort, icon, status) VALUES 
('数码电器', 0, 1, 1, 'icon-digital', 1),
('服装鞋包', 0, 1, 2, 'icon-clothes', 1),
('家居生活', 0, 1, 3, 'icon-home', 1),
('美妆护肤', 0, 1, 4, 'icon-beauty', 1),
('运动户外', 0, 1, 5, 'icon-sports', 1);

-- 插入二级分类
INSERT INTO category (name, parent_id, level, sort, status) VALUES 
('手机通讯', 1, 2, 1, 1),
('电脑办公', 1, 2, 2, 1),
('数码配件', 1, 2, 3, 1),
('男装', 2, 2, 1, 1),
('女装', 2, 2, 2, 1),
('鞋靴', 2, 2, 3, 1),
('家具', 3, 2, 1, 1),
('家电', 3, 2, 2, 1),
('家纺', 3, 2, 3, 1);

-- 插入商品初始数据
INSERT INTO product (name, subtitle, main_image, price, stock, category_id, status) VALUES 
('iPhone 15 Pro', '最新款苹果手机，性能强劲，拍照出色', 'https://img.yzcdn.cn/vant/cat.jpeg', 7999.00, 100, 6, 1),
('MacBook Pro 14英寸', '专业级笔记本电脑，M3芯片，性能卓越', 'https://img.yzcdn.cn/vant/cat.jpeg', 12999.00, 50, 7, 1),
('小米13 Ultra', '徕卡影像，专业摄影手机', 'https://img.yzcdn.cn/vant/cat.jpeg', 4999.00, 80, 6, 1),
('华为P60 Pro', '华为旗舰手机，麒麟芯片', 'https://img.yzcdn.cn/vant/cat.jpeg', 5999.00, 60, 6, 1),
('联想拯救者R9000P', '游戏本，RTX4060显卡', 'https://img.yzcdn.cn/vant/cat.jpeg', 8999.00, 30, 7, 1),
('iPad Air', '轻薄平板电脑，M1芯片', 'https://img.yzcdn.cn/vant/cat.jpeg', 4399.00, 70, 7, 1),
('AirPods Pro', '主动降噪无线耳机', 'https://img.yzcdn.cn/vant/cat.jpeg', 1499.00, 200, 8, 1),
('男士休闲裤', '舒适透气，四季百搭', 'https://img.yzcdn.cn/vant/cat.jpeg', 299.00, 500, 9, 1),
('女士连衣裙', '优雅气质，职场首选', 'https://img.yzcdn.cn/vant/cat.jpeg', 399.00, 300, 10, 1),
('运动鞋', '减震透气，运动必备', 'https://img.yzcdn.cn/vant/cat.jpeg', 599.00, 400, 11, 1);

-- 创建索引
CREATE INDEX idx_admin_username ON admin(username);
CREATE INDEX idx_user_username ON user(username);
CREATE INDEX idx_category_parent_id ON category(parent_id);
CREATE INDEX idx_product_category_status ON product(category_id, status);

-- 设置字符集
SET NAMES utf8mb4; 