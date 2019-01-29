package com.thomax.bootspring.config;

import com.thomax.bootspring.interceptor.CustomInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@SpringBootConfiguration
public class WebConfig extends WebMvcConfigurationSupport {

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new CustomInterceptor());
    }

}

