package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.Admin;

/**
 * 管理员服务接口
 */
public interface AdminService extends IService<Admin> {

    /**
     * 管理员登录
     */
    String login(String username, String password);

    /**
     * 根据用户名获取管理员信息
     */
    Admin getByUsername(String username);

    /**
     * 获取管理员信息（不包含密码）
     */
    Admin getAdminInfo(String username);

    /**
     * 重置管理员密码
     */
    boolean resetPassword(String username, String newPassword);
} 