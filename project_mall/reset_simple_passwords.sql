-- 重置为简单密码的SQL脚本
USE mall;

-- 清理并重新创建管理员数据（使用明文密码）
DELETE FROM admin WHERE username = 'admin';
INSERT INTO admin (username, password, nickname, email, status, deleted) VALUES 
('admin', '123456', '系统管理员', 'admin@mall.com', 1, 0);

-- 清理并重新创建测试用户数据（使用明文密码）
DELETE FROM user WHERE username IN ('user001', 'user002');
INSERT INTO user (username, password, nickname, email, phone, gender, status, deleted) VALUES 
('user001', '123456', '张三', 'zhangsan@example.com', '13800138001', 1, 1, 0),
('user002', '123456', '李四', 'lisi@example.com', '13800138002', 2, 1, 0);

-- 验证数据
SELECT '=== 管理员账号 ===' as info;
SELECT username, password, nickname, status FROM admin WHERE username = 'admin';

SELECT '=== 用户账号 ===' as info;
SELECT username, password, nickname, status FROM user WHERE username IN ('user001', 'user002');

SELECT '
数据重置完成！
管理员账号：admin / 123456
用户账号：user001 / 123456
用户账号：user002 / 123456

首次登录时，明文密码会自动转换为BCrypt加密
' as message; 