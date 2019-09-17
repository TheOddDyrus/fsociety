package com.thomax.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class Config {

    /**
     * http请求地址和资源文件映射如下:  http://localhost:8888/thomax-provider/dev
     * /{application}/{profile}[/{label}]
     * /{application}-{profile}.yml
     * /{label}/{application}-{profile}.yml
     * /{application}-{profile}.properties
     * /{label}/{application}-{profile}.properties
     */
    public static void main(String[] args) {
        SpringApplication.run(Config.class, args);
    }

}
