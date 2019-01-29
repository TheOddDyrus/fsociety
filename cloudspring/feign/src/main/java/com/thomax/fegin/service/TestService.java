package com.thomax.fegin.service;

import com.thomax.fegin.service.impl.TestServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "thomax-provider", fallback = TestServiceImpl.class)
public interface TestService {

    @RequestMapping("/test")
    String exec(@RequestParam("t") String t);

}
