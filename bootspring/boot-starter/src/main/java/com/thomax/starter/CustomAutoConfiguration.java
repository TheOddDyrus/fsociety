package com.thomax.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({CustomAutoConfiguration.class})
@EnableConfigurationProperties(CustomProperties.class)
public class CustomAutoConfiguration {

    @Autowired
    private CustomProperties customProperties;

    @Bean
    @ConditionalOnMissingBean(CustomService.class)
    public CustomService customService() {
        CustomService customService = new CustomService();
        customService.setPrefix(customProperties.getStr());
        return customService;
    }

}
