package com.thomax.example.config;

import com.thomax.example.service.AA;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class SysConfig {

    @Bean(name = "tt")
    public Object createBean() {

        return new AAImpl();
    }

}

class AAImpl implements AA {

    @Override
    public String test() {
        return "AA";
    }
}

