-- 修复现有数据库的SQL脚本
-- 如果你已经创建了数据库，运行此脚本来添加缺失的字段

USE mall;

-- 检查并添加admin表的deleted字段
ALTER TABLE admin ADD COLUMN IF NOT EXISTS deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除';

-- 检查并添加user表的deleted字段（如果没有的话）
ALTER TABLE user ADD COLUMN IF NOT EXISTS deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除';

-- 更新现有记录的deleted字段为0
UPDATE admin SET deleted = 0 WHERE deleted IS NULL;
UPDATE user SET deleted = 0 WHERE deleted IS NULL;

SELECT '数据库修复完成' as message; 