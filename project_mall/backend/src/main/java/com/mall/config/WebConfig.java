package com.mall.config;

import com.mall.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Web配置类
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Value("${file.upload.path:/uploads/}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取绝对上传路径
        Path absoluteUploadPath = getAbsoluteUploadPath();
        String fileUrlPath = "file:" + absoluteUploadPath.toString();
        
        // 确保路径以斜杠结尾
        if (!fileUrlPath.endsWith("/")) {
            fileUrlPath += "/";
        }
        
        log.info("配置文件上传路径: /uploads/** -> {}", fileUrlPath);
        
        // 配置上传文件的访问路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(fileUrlPath);
        
        // 配置静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/admin/login",
                        "/api/user/login",
                        "/api/user/register",
                        "/admin/upload/**",
                        "/uploads/**",
                        "/static/**",
                        "/error",
                        "/actuator/**"
                );
    }

    /**
     * 获取绝对上传路径
     */
    private Path getAbsoluteUploadPath() {
        Path path = Paths.get(uploadPath);
        
        if (path.isAbsolute()) {
            return path;
        } else {
            // 相对路径转换为绝对路径（相对于项目根目录）
            String userDir = System.getProperty("user.dir");
            return Paths.get(userDir, uploadPath);
        }
    }
} 