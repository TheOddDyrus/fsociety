package com.thomax.mockito.service.impl;

import com.thomax.mockito.domain.CustomObject;
import com.thomax.mockito.mapper.CustomMapper;
import com.thomax.mockito.service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomServiceImpl implements CustomService {

    @Autowired
    private CustomMapper customMapper;

    public String test01(String val){
        return customMapper.test01(val);
    }

    @Override
    public String test02(CustomObject object) {
        return customMapper.test02(object);
    }

}
