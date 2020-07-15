package com.thomax.boot.example.config;

import com.thomax.boot.example.interceptor.CustomInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootConfiguration
public class WebConfig extends WebMvcConfigurationSupport {

    /**
     * 添加拦截器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new CustomInterceptor());
    }

}

