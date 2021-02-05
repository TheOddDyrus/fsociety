package com.thomax.ast.model2;

import java.io.Serializable;

public class DeviceDetailInfoTransfer implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long deviceId;
    private String groupName;

    public DeviceDetailInfoTransfer() {
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
