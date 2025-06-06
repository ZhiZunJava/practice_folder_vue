package com.mall.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传服务
 */
@Slf4j
@Service
public class FileUploadService {

    @Value("${file.upload.path:/uploads/}")
    private String uploadPath;
    
    @Value("${file.upload.base-url:http://localhost:8050/api/uploads/}")
    private String baseUrl;

    // 支持的图片格式
    private static final List<String> SUPPORTED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    // 支持的图片扩展名
    private static final List<String> SUPPORTED_IMAGE_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );

    /**
     * 上传文件
     */
    public UploadResult uploadFile(MultipartFile file, String subDir) throws IOException {
        validateFile(file);
        
        // 创建上传目录结构
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fullSubDir = subDir != null ? subDir + "/" + datePath : datePath;
        
        Path uploadDir = createUploadDirectory(fullSubDir);
        
        // 生成文件名和保存文件
        String fileName = generateFileName(file.getOriginalFilename());
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        // 生成访问URL
        String relativePath = fullSubDir + "/" + fileName;
        String fileUrl = baseUrl + relativePath;
        
        log.info("文件上传成功: {} -> {}", file.getOriginalFilename(), filePath);
        
        return UploadResult.builder()
                .url(fileUrl)
                .fileName(fileName)
                .originalName(file.getOriginalFilename())
                .size(file.getSize())
                .contentType(file.getContentType())
                .relativePath(relativePath)
                .absolutePath(filePath.toString())
                .build();
    }

    /**
     * 上传图片
     */
    public UploadResult uploadImage(MultipartFile file) throws IOException {
        validateImageFile(file);
        return uploadFile(file, "images");
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        if (file.getOriginalFilename() == null || file.getOriginalFilename().trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        
        // 检查文件大小（最大10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小不能超过10MB");
        }
    }

    /**
     * 验证图片文件
     */
    private void validateImageFile(MultipartFile file) {
        validateFile(file);
        
        // 检查MIME类型
        String contentType = file.getContentType();
        if (contentType == null || !SUPPORTED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("不支持的图片格式，只支持: " + String.join(", ", SUPPORTED_IMAGE_TYPES));
        }
        
        // 检查文件扩展名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extension = getFileExtension(originalFilename).toLowerCase();
            if (!SUPPORTED_IMAGE_EXTENSIONS.contains(extension)) {
                throw new IllegalArgumentException("不支持的图片扩展名，只支持: " + String.join(", ", SUPPORTED_IMAGE_EXTENSIONS));
            }
        }
        
        // 图片文件大小限制（最大5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("图片文件不能超过5MB");
        }
    }

    /**
     * 创建上传目录
     */
    private Path createUploadDirectory(String subDir) throws IOException {
        Path basePath = getAbsoluteUploadPath();
        Path targetDir = basePath.resolve(subDir);
        
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
            log.info("创建上传目录: {}", targetDir);
        }
        
        return targetDir;
    }

    /**
     * 生成唯一文件名
     */
    private String generateFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        return UUID.randomUUID().toString() + extension;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex) : "";
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

    /**
     * 删除文件
     */
    public boolean deleteFile(String relativePath) {
        try {
            Path basePath = getAbsoluteUploadPath();
            Path filePath = basePath.resolve(relativePath);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("删除文件成功: {}", filePath);
                return true;
            } else {
                log.warn("文件不存在: {}", filePath);
                return false;
            }
        } catch (IOException e) {
            log.error("删除文件失败: {}", relativePath, e);
            return false;
        }
    }

    /**
     * 文件上传结果
     */
    public static class UploadResult {
        private String url;
        private String fileName;
        private String originalName;
        private Long size;
        private String contentType;
        private String relativePath;
        private String absolutePath;

        // Builder模式
        public static UploadResultBuilder builder() {
            return new UploadResultBuilder();
        }

        public static class UploadResultBuilder {
            private String url;
            private String fileName;
            private String originalName;
            private Long size;
            private String contentType;
            private String relativePath;
            private String absolutePath;

            public UploadResultBuilder url(String url) {
                this.url = url;
                return this;
            }

            public UploadResultBuilder fileName(String fileName) {
                this.fileName = fileName;
                return this;
            }

            public UploadResultBuilder originalName(String originalName) {
                this.originalName = originalName;
                return this;
            }

            public UploadResultBuilder size(Long size) {
                this.size = size;
                return this;
            }

            public UploadResultBuilder contentType(String contentType) {
                this.contentType = contentType;
                return this;
            }

            public UploadResultBuilder relativePath(String relativePath) {
                this.relativePath = relativePath;
                return this;
            }

            public UploadResultBuilder absolutePath(String absolutePath) {
                this.absolutePath = absolutePath;
                return this;
            }

            public UploadResult build() {
                UploadResult result = new UploadResult();
                result.url = this.url;
                result.fileName = this.fileName;
                result.originalName = this.originalName;
                result.size = this.size;
                result.contentType = this.contentType;
                result.relativePath = this.relativePath;
                result.absolutePath = this.absolutePath;
                return result;
            }
        }

        // Getters
        public String getUrl() { return url; }
        public String getFileName() { return fileName; }
        public String getOriginalName() { return originalName; }
        public Long getSize() { return size; }
        public String getContentType() { return contentType; }
        public String getRelativePath() { return relativePath; }
        public String getAbsolutePath() { return absolutePath; }
    }
} 