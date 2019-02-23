package com.thomax.example.dao;

import com.thomax.example.model.SyncOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SyncOperationDao {

    /**
     * 根据操作人员姓名，开始时间，结束时间查询同步操作记录
     * @param userName
     * @param pageSize
     * @param beginTime
     * @param endTime
     * @return
     */
    List<SyncOperation> getAllByCondition(@Param("userName") String userName, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                          @Param("beginTime") Date beginTime);

    /**
     * 根据条件查询出总页数
     * @return
     */
    Integer getCountByCondition(@Param("userName") String userName, @Param("beginTime") Date beginTime);

}
