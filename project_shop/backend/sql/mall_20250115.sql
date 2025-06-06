-- ----------------------------
-- 微商城数据库表结构
-- ----------------------------

-- ----------------------------
-- 1、商品分类表
-- ----------------------------
DROP TABLE IF EXISTS `mall_category`;
CREATE TABLE `mall_category` (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父分类ID',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `category_icon` varchar(200) DEFAULT '' COMMENT '分类图标',
  `category_desc` varchar(500) DEFAULT '' COMMENT '分类描述',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='商品分类表';

-- ----------------------------
-- 初始化商品分类数据
-- ----------------------------
INSERT INTO `mall_category` VALUES 
(1, 0, '数码电器', '/profile/upload/category/digital.png', '数码电器类商品', 1, '0', '0', 'admin', now(), '', NULL),
(2, 0, '服装服饰', '/profile/upload/category/clothing.png', '服装服饰类商品', 2, '0', '0', 'admin', now(), '', NULL),
(3, 0, '家居生活', '/profile/upload/category/home.png', '家居生活类商品', 3, '0', '0', 'admin', now(), '', NULL),
(4, 0, '美食特产', '/profile/upload/category/food.png', '美食特产类商品', 4, '0', '0', 'admin', now(), '', NULL),
(5, 1, '手机', '/profile/upload/category/phone.png', '手机类商品', 1, '0', '0', 'admin', now(), '', NULL),
(6, 1, '电脑', '/profile/upload/category/computer.png', '电脑类商品', 2, '0', '0', 'admin', now(), '', NULL);

-- ----------------------------
-- 2、商品表
-- ----------------------------
DROP TABLE IF EXISTS `mall_product`;
CREATE TABLE `mall_product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `product_short_name` varchar(100) DEFAULT '' COMMENT '商品简称',
  `product_desc` text COMMENT '商品描述',
  `product_image` varchar(500) DEFAULT '' COMMENT '商品主图',
  `product_images` text COMMENT '商品图片（多张）',
  `original_price` decimal(10,2) DEFAULT '0.00' COMMENT '原价',
  `current_price` decimal(10,2) NOT NULL COMMENT '现价',
  `stock` int(11) DEFAULT '0' COMMENT '库存',
  `sales` int(11) DEFAULT '0' COMMENT '销量',
  `tag` varchar(100) DEFAULT '' COMMENT '商品标签',
  `is_recommend` char(1) DEFAULT '0' COMMENT '是否推荐（0否 1是）',
  `is_new` char(1) DEFAULT '0' COMMENT '是否新品（0否 1是）',
  `is_hot` char(1) DEFAULT '0' COMMENT '是否热销（0否 1是）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='商品表';

-- ----------------------------
-- 初始化商品数据
-- ----------------------------
INSERT INTO `mall_product` VALUES 
(1, 5, 'iPhone 15 Pro', 'iPhone 15 Pro', 'Apple iPhone 15 Pro 512GB 天然钛色', '/profile/upload/product/iphone15pro.jpg', '["/profile/upload/product/iphone15pro_1.jpg","/profile/upload/product/iphone15pro_2.jpg"]', 9999.00, 8999.00, 100, 50, '新品,热销', '1', '1', '1', '0', '0', 'admin', now(), '', NULL, '最新iPhone'),
(2, 6, 'MacBook Pro', 'MacBook Pro', 'Apple MacBook Pro 14英寸 M3芯片', '/profile/upload/product/macbookpro.jpg', '["/profile/upload/product/macbookpro_1.jpg","/profile/upload/product/macbookpro_2.jpg"]', 15999.00, 14999.00, 50, 20, '推荐', '1', '0', '1', '0', '0', 'admin', now(), '', NULL, '性能强劲'),
(3, 2, '时尚外套', '时尚外套', '2024秋冬新款时尚休闲外套', '/profile/upload/product/jacket.jpg', '["/profile/upload/product/jacket_1.jpg","/profile/upload/product/jacket_2.jpg"]', 299.00, 199.00, 200, 100, '特价', '1', '1', '0', '0', '0', 'admin', now(), '', NULL, '舒适保暖'),
(4, 3, '智能音箱', '智能音箱', '小米小爱同学智能音箱Pro', '/profile/upload/product/speaker.jpg', '["/profile/upload/product/speaker_1.jpg"]', 399.00, 299.00, 150, 80, '智能', '1', '0', '1', '0', '0', 'admin', now(), '', NULL, '语音控制'),
(5, 4, '有机茶叶', '有机茶叶', '福建安溪铁观音茶叶礼盒装', '/profile/upload/product/tea.jpg', '["/profile/upload/product/tea_1.jpg","/profile/upload/product/tea_2.jpg"]', 168.00, 128.00, 300, 150, '有机,健康', '1', '0', '0', '0', '0', 'admin', now(), '', NULL, '香气浓郁');

-- ----------------------------
-- 3、购物车表
-- ----------------------------
DROP TABLE IF EXISTS `mall_cart`;
CREATE TABLE `mall_cart` (
  `cart_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `quantity` int(11) NOT NULL DEFAULT '1' COMMENT '商品数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`cart_id`),
  UNIQUE KEY `uk_user_product` (`user_id`,`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='购物车表';

-- ----------------------------
-- 4、订单表
-- ----------------------------
DROP TABLE IF EXISTS `mall_order`;
CREATE TABLE `mall_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `actual_amount` decimal(10,2) NOT NULL COMMENT '实际支付金额',
  `order_status` char(1) DEFAULT '0' COMMENT '订单状态（0待付款 1已付款 2已发货 3已完成 4已取消）',
  `pay_status` char(1) DEFAULT '0' COMMENT '支付状态（0未支付 1已支付）',
  `pay_type` char(1) DEFAULT '1' COMMENT '支付方式（1微信 2支付宝 3货到付款）',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `receiver_address` varchar(200) NOT NULL COMMENT '收货地址',
  `express_company` varchar(50) DEFAULT '' COMMENT '快递公司',
  `express_no` varchar(100) DEFAULT '' COMMENT '快递单号',
  `order_remark` varchar(500) DEFAULT '' COMMENT '订单备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `ship_time` datetime DEFAULT NULL COMMENT '发货时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 COMMENT='订单表';

-- ----------------------------
-- 5、订单详情表
-- ----------------------------
DROP TABLE IF EXISTS `mall_order_item`;
CREATE TABLE `mall_order_item` (
  `item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT '' COMMENT '商品图片',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `quantity` int(11) NOT NULL COMMENT '购买数量',
  `total_price` decimal(10,2) NOT NULL COMMENT '小计金额',
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 COMMENT='订单详情表';

-- ----------------------------
-- 6、用户地址表
-- ----------------------------
DROP TABLE IF EXISTS `mall_user_address`;
CREATE TABLE `mall_user_address` (
  `address_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `province` varchar(50) NOT NULL COMMENT '省份',
  `city` varchar(50) NOT NULL COMMENT '城市',
  `district` varchar(50) NOT NULL COMMENT '区县',
  `detail_address` varchar(200) NOT NULL COMMENT '详细地址',
  `is_default` char(1) DEFAULT '0' COMMENT '是否默认地址（0否 1是）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`address_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='用户地址表';

-- ----------------------------
-- 7、商品评价表
-- ----------------------------
DROP TABLE IF EXISTS `mall_product_review`;
CREATE TABLE `mall_product_review` (
  `review_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `rating` int(1) NOT NULL DEFAULT '5' COMMENT '评分（1-5星）',
  `review_content` text COMMENT '评价内容',
  `review_images` text COMMENT '评价图片',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`review_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='商品评价表';

-- ----------------------------
-- 8、系统配置表（商城设置）
-- ----------------------------
DROP TABLE IF EXISTS `mall_config`;
CREATE TABLE `mall_config` (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` varchar(100) NOT NULL COMMENT '配置名称',
  `config_key` varchar(100) NOT NULL COMMENT '配置键名',
  `config_value` text COMMENT '配置值',
  `config_type` char(1) DEFAULT 'N' COMMENT '配置类型（Y系统内置 N用户自定义）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='商城配置表';

-- ----------------------------
-- 初始化商城配置数据
-- ----------------------------
INSERT INTO `mall_config` VALUES 
(1, '商城名称', 'mall.name', '若依微商城', 'Y', 'admin', now(), '', NULL, '商城名称配置'),
(2, '商城Logo', 'mall.logo', '/profile/upload/logo.png', 'Y', 'admin', now(), '', NULL, '商城Logo配置'),
(3, '客服电话', 'mall.phone', '400-888-8888', 'Y', 'admin', now(), '', NULL, '客服电话配置'),
(4, '免运费金额', 'mall.free.shipping.amount', '99', 'Y', 'admin', now(), '', NULL, '满多少元免运费'),
(5, '运费', 'mall.shipping.fee', '10', 'Y', 'admin', now(), '', NULL, '默认运费');

-- 菜单 SQL
INSERT INTO `sys_menu` VALUES (2000, '微商城', 0, 5, 'mall', NULL, '', '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', now(), '', NULL, '微商城目录');

-- 商品管理菜单
INSERT INTO `sys_menu` VALUES (2001, '商品管理', 2000, 1, 'product', 'mall/product/index', '', '', 1, 0, 'C', '0', '0', 'mall:product:list', 'goods', 'admin', now(), '', NULL, '商品管理菜单');
INSERT INTO `sys_menu` VALUES (2002, '商品查询', 2001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:product:query', '#', 'admin', now(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2003, '商品新增', 2001, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:product:add', '#', 'admin', now(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2004, '商品修改', 2001, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:product:edit', '#', 'admin', now(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2005, '商品删除', 2001, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:product:remove', '#', 'admin', now(), '', NULL, '');

-- 分类管理菜单
INSERT INTO `sys_menu` VALUES (2010, '分类管理', 2000, 2, 'category', 'mall/category/index', '', '', 1, 0, 'C', '0', '0', 'mall:category:list', 'tree', 'admin', now(), '', NULL, '分类管理菜单');
INSERT INTO `sys_menu` VALUES (2011, '分类查询', 2010, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:category:query', '#', 'admin', now(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2012, '分类新增', 2010, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:category:add', '#', 'admin', now(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2013, '分类修改', 2010, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:category:edit', '#', 'admin', now(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2014, '分类删除', 2010, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:category:remove', '#', 'admin', now(), '', NULL, '');

-- 订单管理菜单
INSERT INTO `sys_menu` VALUES (2020, '订单管理', 2000, 3, 'order', 'mall/order/index', '', '', 1, 0, 'C', '0', '0', 'mall:order:list', 'list', 'admin', now(), '', NULL, '订单管理菜单');
INSERT INTO `sys_menu` VALUES (2021, '订单查询', 2020, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:order:query', '#', 'admin', now(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2022, '订单详情', 2020, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:order:detail', '#', 'admin', now(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2023, '订单发货', 2020, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:order:ship', '#', 'admin', now(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2024, '订单完成', 2020, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:order:complete', '#', 'admin', now(), '', NULL, '');

-- 商城配置菜单
INSERT INTO `sys_menu` VALUES (2030, '商城配置', 2000, 4, 'config', 'mall/config/index', '', '', 1, 0, 'C', '0', '0', 'mall:config:list', 'edit', 'admin', now(), '', NULL, '商城配置菜单');
INSERT INTO `sys_menu` VALUES (2031, '配置查询', 2030, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:config:query', '#', 'admin', now(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2032, '配置修改', 2030, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'mall:config:edit', '#', 'admin', now(), '', NULL, ''); 