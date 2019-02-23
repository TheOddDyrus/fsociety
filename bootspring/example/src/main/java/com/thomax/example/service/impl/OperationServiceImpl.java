package com.thomax.example.service.impl;

import com.thomax.example.dao.SyncOperationDao;
import com.thomax.example.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private SyncOperationDao syncOperationDao;

    @Override
    public Integer getTotal() {
        return syncOperationDao.getCountByCondition(null, null);
    }
}
