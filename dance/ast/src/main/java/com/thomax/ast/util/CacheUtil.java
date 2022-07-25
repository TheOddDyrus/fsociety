package com.thomax.ast.util;

import com.thomax.ast.model2.DeviceDetailInfoTransfer;

/**
 * 项目内缓存工具类
 */
public class CacheUtil {

    public static DeviceDetailInfoTransfer getDeviceDetailCache(Long deviceId) {
        return new DeviceDetailInfoTransfer(); //从缓存中获得此对象
    }

}
