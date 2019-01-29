package com.thomax.bootspring.service.impl;

import com.thomax.bootspring.dao.SyncOperationDao;
import com.thomax.bootspring.service.OperationService;
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
