package com.thomax.starter;

import com.alibaba.fastjson.JSON;

public class CustomService {

    private String prefix = "";

    public String toJsonString(Object obj) {
        return prefix + JSON.toJSONString(obj);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
