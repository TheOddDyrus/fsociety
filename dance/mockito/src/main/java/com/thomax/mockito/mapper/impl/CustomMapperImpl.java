package com.thomax.mockito.mapper.impl;

import com.thomax.mockito.domain.CustomObject;
import com.thomax.mockito.mapper.CustomMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomMapperImpl implements CustomMapper {

    @Override
    public String test01(String val) {
        return val;
    }

    @Override
    public String test02(CustomObject object) {
        return object.toString();
    }

}
