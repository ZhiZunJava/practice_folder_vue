package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     */
    String login(String username, String password);

    /**
     * 用户注册
     */
    boolean register(User user);

    /**
     * 根据用户名获取用户信息
     */
    User getByUsername(String username);

    /**
     * 获取用户信息（不包含密码）
     */
    User getUserInfo(String username);

    /**
     * 更新用户信息
     */
    boolean updateUserInfo(User user);

    /**
     * 修改密码
     */
    boolean changePassword(String username, String oldPassword, String newPassword);

    /**
     * 重置用户密码
     */
    boolean resetPassword(String username, String newPassword);
} 