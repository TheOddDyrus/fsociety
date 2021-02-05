package com.thomax.ast.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.thomax.ast.model2.DeviceDetailInfoTransfer;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 项目内缓存工具类
 */
public class CacheUtil {

    private Long deviceCacheTotal;

    private Long deviceCacheExpire;

    private Long astCacheTotal;

    private Long astCacheExpire;

    private static LoadingCache<Long, DeviceDetailInfoTransfer> deviceCache;

    @PostConstruct
    public void init() {
        //初始化设备详情缓存
        deviceCache = CacheBuilder.newBuilder()
                .maximumSize(deviceCacheTotal)
                .expireAfterWrite(deviceCacheExpire, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, DeviceDetailInfoTransfer>() {
                    @Override
                    public DeviceDetailInfoTransfer load(Long key) {
                        List<DeviceDetailInfoTransfer> list = new ArrayList<>();//deviceService.getDeviceDetail4Transfer(Collections.singletonList(key));
                        if (CollectionUtils.isNotEmpty(list)) {
                            return list.get(0);
                        }

                        return null;
                    }
                });
    }

    public static DeviceDetailInfoTransfer getDeviceDetailCache(Long deviceId) {
        try {
            return deviceCache.get(deviceId);
        } catch (ExecutionException e1) {
            try {
                return deviceCache.get(deviceId);
            } catch (ExecutionException e2) {
                //最多只查2次
            }
        }

        return null;
    }

}
