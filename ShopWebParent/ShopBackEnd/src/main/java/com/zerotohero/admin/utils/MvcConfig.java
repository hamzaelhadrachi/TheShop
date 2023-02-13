package com.zerotohero.admin.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "user-photo";
        Path userPhotoDir = Paths.get(dirName);

        String userPhotoPAth = userPhotoDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/"+ dirName+ "/**")
                .addResourceLocations("file:/"+userPhotoPAth+"/");
    }
}
