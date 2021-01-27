package com.thomax.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "thomax-starter-prop")
public class CustomProperties {

    private String str = "thomax test#";

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

}
