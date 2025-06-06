package com.mall.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件上传配置
 */
@Slf4j
@Component
public class FileUploadConfig implements CommandLineRunner {

    @Value("${file.upload.path:/uploads/}")
    private String uploadPath;

    @Override
    public void run(String... args) throws Exception {
        initUploadDirectory();
    }

    /**
     * 初始化上传目录
     */
    private void initUploadDirectory() {
        try {
            Path path = getAbsoluteUploadPath();
            
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            // 创建子目录
            Path imagesDir = path.resolve("images");
            if (!Files.exists(imagesDir)) {
                Files.createDirectories(imagesDir);
                log.info("创建图片上传目录: {}", imagesDir.toAbsolutePath());
            }
            
            // 检查目录权限
            if (!Files.isWritable(path)) {
                log.warn("上传目录不可写: {}", path.toAbsolutePath());
            }
            
        } catch (Exception e) {
            log.error("初始化上传目录失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取绝对上传路径
     */
    private Path getAbsoluteUploadPath() {
        Path path = Paths.get(uploadPath);
        
        if (path.isAbsolute()) {
            return path;
        } else {
            // 相对路径转换为绝对路径
            String userDir = System.getProperty("user.dir");
            return Paths.get(userDir, uploadPath);
        }
    }
} 