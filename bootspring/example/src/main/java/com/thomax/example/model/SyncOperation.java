package com.thomax.example.model;

import java.io.Serializable;
import java.util.Date;

public class SyncOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    //同步操作id
    private Integer id;

    //同步操作者id
    private Integer userId;

    //同步操作时间
    private Date operationTime;

    //同步操作的类型
    private Integer operationType;

    //同步操作的数据
    private String operationData;

    //是否同步成功
    private Integer isSuccess;

    private String realName;  //同步操作者真实姓名

    private String operationTypeName; //同步操作类型名

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getOperationData() {
        return operationData;
    }

    public void setOperationData(String operationData) {
        this.operationData = operationData;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOperationTypeName() {
        return operationTypeName;
    }

    public void setOperationTypeName(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }
}
