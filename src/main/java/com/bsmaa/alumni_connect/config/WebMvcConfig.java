package com.bsmaa.alumni_connect.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = uploadDir;
        if (!path.endsWith("/")) {
            path += "/";
        }

        String resourceLocation;
        if (path.startsWith("/") || path.contains(":/") || path.contains(":\\")) {
            resourceLocation = "file:" + path;
        } else {
            resourceLocation = "file:" + new File(path).getAbsolutePath() + "/";
        }

        registry.addResourceHandler("/images/uploads/**")
                .addResourceLocations(resourceLocation);
    }
}
