package com.kumara.backed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Buat folder bernama 'user-photos' di root project kamu nanti
        Path uploadDir = Paths.get("./user-photos");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/photos/**")
                .addResourceLocations("file:/" + uploadPath + "/");
    }
}