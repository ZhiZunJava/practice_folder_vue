package com.mall.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;

/**
 * JWT工具类
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey secretKey;

    /**
     * 获取安全的密钥
     */
    private SecretKey getSecretKey() {
        if (secretKey == null) {
            try {
                // 方法1：如果配置的密钥够长，直接使用
                if (secret.getBytes(StandardCharsets.UTF_8).length >= 32) {
                    secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    log.info("使用配置的JWT密钥");
                } else {
                    // 方法2：使用SHA-256对短密钥进行哈希处理，确保长度够用
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    byte[] hashedKey = md.digest(secret.getBytes(StandardCharsets.UTF_8));
                    secretKey = Keys.hmacShaKeyFor(hashedKey);
                    log.info("使用SHA-256哈希处理后的JWT密钥");
                }
            } catch (Exception e) {
                // 方法3：如果上述都失败，生成一个安全的随机密钥
                secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                log.warn("JWT密钥配置有问题，使用随机生成的安全密钥: {}", e.getMessage());
            }
        }
        return secretKey;
    }

    /**
     * 生成JWT Token
     */
    public String generateToken(String username, String userType) {
        try {
            Date now = new Date();
            Date expirationDate = new Date(now.getTime() + expiration * 1000);

            String token = Jwts.builder()
                    .setSubject(username)
                    .claim("userType", userType)
                    .setIssuedAt(now)
                    .setExpiration(expirationDate)
                    .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                    .compact();

            log.debug("生成JWT Token成功，用户: {}, 类型: {}", username, userType);
            return token;
        } catch (Exception e) {
            log.error("生成JWT Token失败，用户: {}, 错误: {}", username, e.getMessage());
            throw new RuntimeException("生成Token失败", e);
        }
    }

    /**
     * 从Token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            return getClaimsFromToken(token).getSubject();
        } catch (Exception e) {
            log.error("从Token获取用户名失败: {}", e.getMessage());
            throw new RuntimeException("Token解析失败", e);
        }
    }

    /**
     * 从Token中获取用户类型
     */
    public String getUserTypeFromToken(String token) {
        try {
            return (String) getClaimsFromToken(token).get("userType");
        } catch (Exception e) {
            log.error("从Token获取用户类型失败: {}", e.getMessage());
            throw new RuntimeException("Token解析失败", e);
        }
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            log.debug("Token验证成功，用户: {}", claims.getSubject());
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("不支持的Token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Token格式错误: {}", e.getMessage());
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("Token签名错误: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Token为空: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Token验证异常: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 从Token中获取Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查Token是否即将过期
     */
    public boolean isTokenExpiringSoon(String token) {
        try {
            Date expiration = getClaimsFromToken(token).getExpiration();
            Date now = new Date();
            boolean expiringSoon = expiration.getTime() - now.getTime() < 300000; // 5分钟内过期
            if (expiringSoon) {
                log.debug("Token即将过期，剩余时间: {} 毫秒", expiration.getTime() - now.getTime());
            }
            return expiringSoon;
        } catch (Exception e) {
            log.error("检查Token过期时间失败: {}", e.getMessage());
            return true; // 出错时认为即将过期
        }
    }

    /**
     * 获取Token剩余有效时间（毫秒）
     */
    public long getTokenRemainingTime(String token) {
        try {
            Date expiration = getClaimsFromToken(token).getExpiration();
            Date now = new Date();
            return Math.max(0, expiration.getTime() - now.getTime());
        } catch (Exception e) {
            log.error("获取Token剩余时间失败: {}", e.getMessage());
            return 0;
        }
    }
} 