package com.mall.controller.admin;

import com.mall.common.Result;
import com.mall.entity.Admin;
import com.mall.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginForm) {
        try {
            String username = loginForm.get("username");
            String password = loginForm.get("password");
            
            String token = adminService.login(username, password);
            Admin adminInfo = adminService.getAdminInfo(username);
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("adminInfo", adminInfo);
            
            return Result.success("登录成功", data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前登录管理员信息
     */
    @GetMapping("/info")
    public Result<Admin> getInfo(HttpServletRequest request) {
        try {
            // 从JWT Token中获取当前用户名
            String currentUsername = (String) request.getAttribute("currentUsername");
            String currentUserType = (String) request.getAttribute("currentUserType");
            
            if (currentUsername == null) {
                return Result.error("未登录或登录已过期");
            }
            
            if (!"admin".equals(currentUserType)) {
                return Result.error("权限不足");
            }
            
            Admin admin = adminService.getAdminInfo(currentUsername);
            if (admin == null) {
                return Result.error("管理员信息不存在");
            }
            
            return Result.success(admin);
        } catch (Exception e) {
            log.error("获取管理员信息失败", e);
            return Result.error("获取管理员信息失败：" + e.getMessage());
        }
    }

    /**
     * 修改管理员信息
     */
    @PutMapping("/info")
    public Result<Admin> updateInfo(@RequestBody Admin admin, HttpServletRequest request) {
        try {
            // 从JWT Token中获取当前用户名
            String currentUsername = (String) request.getAttribute("currentUsername");
            String currentUserType = (String) request.getAttribute("currentUserType");
            
            if (currentUsername == null) {
                return Result.error("未登录或登录已过期");
            }
            
            if (!"admin".equals(currentUserType)) {
                return Result.error("权限不足");
            }
            
            // 获取当前登录的管理员信息
            Admin currentAdmin = adminService.getByUsername(currentUsername);
            if (currentAdmin == null) {
                return Result.error("管理员信息不存在");
            }
            
            // 只允许修改特定字段，保证安全性
            currentAdmin.setNickname(admin.getNickname());
            currentAdmin.setPhone(admin.getPhone());
            currentAdmin.setEmail(admin.getEmail());
            currentAdmin.setAvatar(admin.getAvatar());
            
            // 更新管理员信息
            boolean success = adminService.updateById(currentAdmin);
            if (!success) {
                return Result.error("更新失败");
            }
            
            // 返回更新后的信息（不包含密码）
            Admin updatedAdmin = adminService.getAdminInfo(currentUsername);
            log.info("管理员信息更新成功: {}", currentUsername);
            
            return Result.success("更新成功", updatedAdmin);
        } catch (Exception e) {
            log.error("更新管理员信息失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<String> changePassword(@RequestBody Map<String, String> passwordForm, HttpServletRequest request) {
        try {
            // 从JWT Token中获取当前用户名
            String currentUsername = (String) request.getAttribute("currentUsername");
            String currentUserType = (String) request.getAttribute("currentUserType");
            
            if (currentUsername == null) {
                return Result.error("未登录或登录已过期");
            }
            
            if (!"admin".equals(currentUserType)) {
                return Result.error("权限不足");
            }
            
            String currentPassword = passwordForm.get("currentPassword");
            String newPassword = passwordForm.get("newPassword");
            
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                return Result.error("当前密码不能为空");
            }
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return Result.error("新密码不能为空");
            }
            
            if (newPassword.length() < 6) {
                return Result.error("新密码长度不能少于6位");
            }
            
            // 验证当前密码
            try {
                adminService.login(currentUsername, currentPassword);
            } catch (Exception e) {
                return Result.error("当前密码错误");
            }
            
            // 修改密码
            boolean success = adminService.resetPassword(currentUsername, newPassword);
            if (!success) {
                return Result.error("密码修改失败");
            }
            
            log.info("管理员密码修改成功: {}", currentUsername);
            return Result.success("密码修改成功");
        } catch (Exception e) {
            log.error("修改密码失败", e);
            return Result.error("密码修改失败：" + e.getMessage());
        }
    }
} 