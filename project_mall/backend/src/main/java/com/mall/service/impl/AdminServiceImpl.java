package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Admin;
import com.mall.mapper.AdminMapper;
import com.mall.service.AdminService;
import com.mall.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 管理员服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public String login(String username, String password) {
        // 参数验证
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        log.info("管理员登录尝试，用户名: {}", username);

        try {
            // 查询管理员
            Admin admin = getByUsername(username);
            if (admin == null) {
                log.warn("管理员不存在: {}", username);
                throw new RuntimeException("用户名或密码错误");
            }

            // 检查账号状态
            if (admin.getStatus() == null || admin.getStatus() == 0) {
                log.warn("管理员账号已被禁用: {}", username);
                throw new RuntimeException("账号已被禁用");
            }

            // 验证密码 - 支持多种格式
            boolean passwordMatches = verifyPassword(password, admin.getPassword(), username);
            
            if (!passwordMatches) {
                log.warn("管理员密码错误: {}", username);
                throw new RuntimeException("用户名或密码错误");
            }

            // 如果密码是明文，则更新为BCrypt加密
            if (!isBCryptPassword(admin.getPassword())) {
                String encryptedPassword = passwordEncoder.encode(password);
                admin.setPassword(encryptedPassword);
                log.info("将管理员 {} 的明文密码更新为BCrypt格式", username);
            }

            // 更新最后登录时间
            admin.setLastLoginTime(LocalDateTime.now());
            updateById(admin);

            // 生成JWT Token
            String token = jwtUtil.generateToken(username, "admin");
            log.info("管理员登录成功: {}", username);
            
            return token;
        } catch (Exception e) {
            log.error("管理员登录失败: {}, 错误: {}", username, e.getMessage());
            throw e;
        }
    }

    /**
     * 验证密码 - 支持明文和BCrypt
     */
    private boolean verifyPassword(String inputPassword, String storedPassword, String username) {
        if (!StringUtils.hasText(storedPassword)) {
            log.error("管理员存储密码为空: {}", username);
            return false;
        }

        try {
            // 检查是否为BCrypt格式
            if (isBCryptPassword(storedPassword)) {
                log.debug("使用BCrypt验证密码，用户: {}", username);
                return passwordEncoder.matches(inputPassword, storedPassword);
            } else {
                // 明文密码比较
                log.debug("使用明文验证密码，用户: {}", username);
                return inputPassword.equals(storedPassword);
            }
        } catch (Exception e) {
            log.error("密码验证异常，用户: {}, 错误: {}", username, e.getMessage());
            return false;
        }
    }

    /**
     * 检查是否为BCrypt密码格式
     */
    private boolean isBCryptPassword(String password) {
        return StringUtils.hasText(password) && 
               password.startsWith("$2a$") && 
               password.length() >= 59; // BCrypt标准长度是60，但有些可能59
    }

    @Override
    public Admin getByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        
        try {
            return getOne(new LambdaQueryWrapper<Admin>()
                    .eq(Admin::getUsername, username));
        } catch (Exception e) {
            log.error("查询管理员失败: {}, 错误: {}", username, e.getMessage(), e);
            throw new RuntimeException("查询管理员信息失败，请检查数据库配置");
        }
    }

    @Override
    public Admin getAdminInfo(String username) {
        Admin admin = getByUsername(username);
        if (admin != null) {
            // 清除密码字段
            admin.setPassword(null);
        }
        return admin;
    }

    /**
     * 重置管理员密码为明文（用于修复数据）
     */
    public boolean resetPassword(String username, String newPassword) {
        try {
            Admin admin = getByUsername(username);
            if (admin == null) {
                return false;
            }
            
            // 加密新密码
            String encryptedPassword = passwordEncoder.encode(newPassword);
            admin.setPassword(encryptedPassword);
            updateById(admin);
            
            log.info("管理员密码重置成功: {}", username);
            return true;
        } catch (Exception e) {
            log.error("重置密码失败: {}, 错误: {}", username, e.getMessage());
            return false;
        }
    }
} 