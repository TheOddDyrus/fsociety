package com.thomax.example.service.impl;

import com.thomax.example.dao.SyncOperationDao;
import com.thomax.example.service.OperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OperationServiceImpl implements OperationService {

    @Resource
    private SyncOperationDao syncOperationDao;

    @Override
    public Integer getTotal() {
        return syncOperationDao.getCountByCondition(null, null);
    }
}
