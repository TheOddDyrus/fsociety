package com.thomax.ribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class TestService {

    @Resource
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "toError")
    public String exec(String t) {
        return restTemplate.getForObject("http://thomax-provider/test?t=" + (StringUtils.isEmpty(t) ? "" : t), String.class);
    }

    public String toError(String t) {
        return "Hystrix fallback" + (StringUtils.isEmpty(t) ? "" : ": " + t);
    }

}
