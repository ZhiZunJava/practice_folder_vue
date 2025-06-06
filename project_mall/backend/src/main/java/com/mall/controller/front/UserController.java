package com.mall.controller.front;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 前台用户控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginForm) {
        try {
            String username = loginForm.get("username");
            String password = loginForm.get("password");
            
            String token = userService.login(username, password);
            User userInfo = userService.getUserInfo(username);
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userInfo", userInfo);
            
            return Result.success("登录成功", data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        try {
            userService.register(user);
            return Result.success("注册成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        try {
            // 从JWT Token中获取当前用户名
            String currentUsername = (String) request.getAttribute("currentUsername");
            String currentUserType = (String) request.getAttribute("currentUserType");
            
            if (currentUsername == null) {
                return Result.error("未登录或登录已过期");
            }
            
            if (!"user".equals(currentUserType)) {
                return Result.error("权限不足");
            }
            
            User user = userService.getUserInfo(currentUsername);
            if (user == null) {
                return Result.error("用户信息不存在");
            }
            
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<String> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        try {
            // 从JWT Token中获取当前用户名
            String currentUsername = (String) request.getAttribute("currentUsername");
            String currentUserType = (String) request.getAttribute("currentUserType");
            
            if (currentUsername == null) {
                return Result.error("未登录或登录已过期");
            }
            
            if (!"user".equals(currentUserType)) {
                return Result.error("权限不足");
            }
            
            // 确保只能修改自己的信息
            user.setUsername(currentUsername);
            user.setId(null); // 防止修改ID
            user.setPassword(null); // 防止通过此接口修改密码
            
            userService.updateById(user);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<String> changePassword(@RequestBody Map<String, String> params) {
        try {
            String username = params.get("username");
            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");
            
            userService.changePassword(username, oldPassword, newPassword);
            return Result.success("密码修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 