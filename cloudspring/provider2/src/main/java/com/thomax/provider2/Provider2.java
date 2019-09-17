package com.thomax.provider2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Provider2 {

    public static void main(String[] args) {
        SpringApplication.run( Provider2.class, args );
    }

}
