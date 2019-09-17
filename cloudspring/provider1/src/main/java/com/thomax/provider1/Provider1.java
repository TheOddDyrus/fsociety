package com.thomax.provider1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Provider1 {

    public static void main(String[] args) {
        SpringApplication.run( Provider1.class, args );
    }

}
