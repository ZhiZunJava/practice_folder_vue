package com.mall.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具
 */
public class GeneratePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String encodedPassword = encoder.encode(password);
        
        System.out.println("原密码: " + password);
        System.out.println("BCrypt加密后: " + encodedPassword);
        System.out.println("格式验证: " + (encodedPassword.startsWith("$2a$") ? "✓ 正确" : "✗ 错误"));
        
        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("密码验证: " + (matches ? "✓ 成功" : "✗ 失败"));
        
        System.out.println("\nSQL更新语句:");
        System.out.println("UPDATE admin SET password = '" + encodedPassword + "' WHERE username = 'admin';");
    }
} 