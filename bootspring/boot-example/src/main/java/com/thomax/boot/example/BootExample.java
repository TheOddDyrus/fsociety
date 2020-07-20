package com.thomax.boot.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.thomax.example.dao")
public class BootExample {

    public static void main(String[] args) {
        SpringApplication.run(BootExample.class, args);
    }

}
