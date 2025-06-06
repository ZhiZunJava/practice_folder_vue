package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.User;
import com.mall.mapper.UserMapper;
import com.mall.service.UserService;
import com.mall.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public String login(String username, String password) {
        // 参数验证
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        log.info("用户登录尝试，用户名: {}", username);

        try {
            // 查询用户
            User user = getByUsername(username);
            if (user == null) {
                log.warn("用户不存在: {}", username);
                throw new RuntimeException("用户名或密码错误");
            }

            // 检查账号状态
            if (user.getStatus() == null || user.getStatus() == 0) {
                log.warn("用户账号已被禁用: {}", username);
                throw new RuntimeException("账号已被禁用");
            }

            // 验证密码 - 支持多种格式
            boolean passwordMatches = verifyPassword(password, user.getPassword(), username);
            
            if (!passwordMatches) {
                log.warn("用户密码错误: {}", username);
                throw new RuntimeException("用户名或密码错误");
            }

            // 如果密码是明文，则更新为BCrypt加密
            if (!isBCryptPassword(user.getPassword())) {
                String encryptedPassword = passwordEncoder.encode(password);
                user.setPassword(encryptedPassword);
                log.info("将用户 {} 的明文密码更新为BCrypt格式", username);
            }

            // 更新最后登录时间
            user.setLastLoginTime(LocalDateTime.now());
            updateById(user);

            // 生成JWT Token
            String token = jwtUtil.generateToken(username, "user");
            log.info("用户登录成功: {}", username);
            
            return token;
        } catch (Exception e) {
            log.error("用户登录失败: {}, 错误: {}", username, e.getMessage());
            throw e;
        }
    }

    /**
     * 验证密码 - 支持明文和BCrypt
     */
    private boolean verifyPassword(String inputPassword, String storedPassword, String username) {
        if (!StringUtils.hasText(storedPassword)) {
            log.error("用户存储密码为空: {}", username);
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
    public boolean register(User user) {
        try {
            // 检查用户名是否已存在
            User existUser = getByUsername(user.getUsername());
            if (existUser != null) {
                throw new RuntimeException("用户名已存在");
            }

            // 加密密码
            if (StringUtils.hasText(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                throw new RuntimeException("密码不能为空");
            }
            
            // 设置默认值
            if (user.getStatus() == null) {
                user.setStatus(1);
            }
            if (user.getGender() == null) {
                user.setGender(0);
            }

            boolean result = save(user);
            log.info("用户注册成功: {}", user.getUsername());
            return result;
        } catch (Exception e) {
            log.error("用户注册失败: {}, 错误: {}", user.getUsername(), e.getMessage());
            throw e;
        }
    }

    @Override
    public User getByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        
        try {
            return getOne(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, username));
        } catch (Exception e) {
            log.error("查询用户失败: {}, 错误: {}", username, e.getMessage());
            throw new RuntimeException("查询用户信息失败，请检查数据库配置");
        }
    }

    @Override
    public User getUserInfo(String username) {
        User user = getByUsername(username);
        if (user != null) {
            // 清除密码字段
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public boolean updateUserInfo(User user) {
        try {
            // 不允许修改密码
            user.setPassword(null);
            boolean result = updateById(user);
            log.info("用户信息更新成功: {}", user.getUsername());
            return result;
        } catch (Exception e) {
            log.error("更新用户信息失败: {}, 错误: {}", user.getUsername(), e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        try {
            User user = getByUsername(username);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            // 验证旧密码
            boolean oldPasswordMatches = verifyPassword(oldPassword, user.getPassword(), username);
            if (!oldPasswordMatches) {
                throw new RuntimeException("原密码错误");
            }

            // 更新密码
            user.setPassword(passwordEncoder.encode(newPassword));
            boolean result = updateById(user);
            log.info("用户密码修改成功: {}", username);
            return result;
        } catch (Exception e) {
            log.error("修改密码失败: {}, 错误: {}", username, e.getMessage());
            throw e;
        }
    }

    /**
     * 重置用户密码
     */
    public boolean resetPassword(String username, String newPassword) {
        try {
            User user = getByUsername(username);
            if (user == null) {
                return false;
            }
            
            // 加密新密码
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            updateById(user);
            
            log.info("用户密码重置成功: {}", username);
            return true;
        } catch (Exception e) {
            log.error("重置密码失败: {}, 错误: {}", username, e.getMessage());
            return false;
        }
    }
} 