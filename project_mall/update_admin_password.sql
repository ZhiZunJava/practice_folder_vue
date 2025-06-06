-- 更新管理员密码的SQL脚本
USE mall;

-- 先添加缺失的deleted字段（如果还没有的话）
ALTER TABLE admin ADD COLUMN IF NOT EXISTS deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除';
ALTER TABLE user ADD COLUMN IF NOT EXISTS deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除';

-- 更新现有记录的deleted字段
UPDATE admin SET deleted = 0 WHERE deleted IS NULL;
UPDATE user SET deleted = 0 WHERE deleted IS NULL;

-- 删除原有的admin用户（如果存在）
DELETE FROM admin WHERE username = 'admin';

-- 插入新的admin用户，密码是123456的BCrypt加密
-- 这个密码是通过BCrypt加密的123456
INSERT INTO admin (username, password, nickname, email, status, deleted) VALUES 
('admin', '$2a$10$7BPn9EnxTh.8CV0NP6fvT.vpUmvxJXsJNfLGcYN6a4a4lO.fqNUEe', '系统管理员', 'admin@mall.com', 1, 0);

SELECT '管理员密码更新完成，用户名：admin，密码：123456' as message; 