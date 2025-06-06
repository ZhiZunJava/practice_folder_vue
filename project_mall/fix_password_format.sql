-- 修复管理员密码格式的SQL脚本
USE mall;

-- 方案一：使用正确格式的BCrypt密码（123456）
UPDATE admin 
SET password = '$2a$10$N.Jp1Lg.VX2mKU4RLOYgL.wt6MJDG2p.z/JZlXdWLPvX.5Ey2MV4u' 
WHERE username = 'admin';

-- 验证更新结果
SELECT username, password, nickname 
FROM admin 
WHERE username = 'admin';

SELECT '密码格式修复完成！用户名：admin，密码：123456' as message; 