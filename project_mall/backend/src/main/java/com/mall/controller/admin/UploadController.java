package com.mall.controller.admin;

import com.mall.common.Result;
import com.mall.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UploadController {

    private final FileUploadService fileUploadService;

    @Value("${file.upload.path:/uploads/}")
    private String uploadPath;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public Result<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileUploadService.UploadResult result = fileUploadService.uploadFile(file, null);
            
            Map<String, Object> data = new HashMap<>();
            data.put("url", result.getUrl());
            data.put("filename", result.getFileName());
            data.put("originalName", result.getOriginalName());
            data.put("size", result.getSize());
            data.put("contentType", result.getContentType());
            
            return Result.success("上传成功", data);
            
        } catch (IllegalArgumentException e) {
            log.warn("文件上传验证失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 上传图片
     */
    @PostMapping("/upload/image")
    public Result<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            FileUploadService.UploadResult result = fileUploadService.uploadImage(file);
            
            Map<String, Object> data = new HashMap<>();
            data.put("url", result.getUrl());
            data.put("filename", result.getFileName());
            data.put("originalName", result.getOriginalName());
            data.put("size", result.getSize());
            data.put("contentType", result.getContentType());
            
            return Result.success("图片上传成功", data);
            
        } catch (IllegalArgumentException e) {
            log.warn("图片上传验证失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("图片上传失败: {}", e.getMessage(), e);
            return Result.error("图片上传失败：" + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/upload")
    public Result<String> deleteFile(@RequestParam("path") String relativePath) {
        try {
            boolean success = fileUploadService.deleteFile(relativePath);
            if (success) {
                return Result.success("文件删除成功");
            } else {
                return Result.error("文件删除失败，文件可能不存在");
            }
        } catch (Exception e) {
            log.error("删除文件异常: {}", e.getMessage(), e);
            return Result.error("删除文件失败：" + e.getMessage());
        }
    }

    /**
     * 获取上传配置信息
     */
    @GetMapping("/upload/config")
    public Result<Map<String, Object>> getUploadConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("maxFileSize", "10MB");
        config.put("maxImageSize", "5MB");
        config.put("supportedImageTypes", new String[]{"jpg", "jpeg", "png", "gif", "webp"});
        config.put("uploadPath", uploadPath);
        
        return Result.success("获取配置成功", config);
    }
} 