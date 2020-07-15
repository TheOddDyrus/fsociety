package com.thomax.boot.example.service.impl;

import com.thomax.boot.example.service.OperationService;
import com.thomax.boot.example.dao.SyncOperationDao;
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
