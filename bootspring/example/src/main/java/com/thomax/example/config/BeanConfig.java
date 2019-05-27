package com.thomax.example.config;

import com.thomax.example.service.impl.AAImpl;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class BeanConfig {

    @Bean(name = "tt")
    public Object createBean() {

        return new AAImpl();
    }

}

