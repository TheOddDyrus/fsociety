package com.thomax.fegin.service.impl;

import com.thomax.fegin.service.TestService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TestServiceImpl implements TestService {

    @Override
    public String exec(String t) {
        return "Hystrix fallback" + (StringUtils.isEmpty(t) ? "" : ": " + t);
    }

}
