package com.smartflow.crrc.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Log4j2
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //允许跨域访问的路径
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS") //允许请求的方法
                .allowedOrigins("*") //允许跨域访问的源
                .maxAge(3600) //预检间隔时间
                .allowCredentials(true) //是否发送cookie
                .allowedHeaders("*");
    }
}
