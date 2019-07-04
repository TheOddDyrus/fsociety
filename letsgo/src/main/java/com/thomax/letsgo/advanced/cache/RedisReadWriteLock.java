package com.thomax.letsgo.advanced.cache;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RedisReadWriteLock {

    private RedissonClient redissonClient;

    private static final Map<Long, ReadWriteLock> LOCK_MAP = new ConcurrentHashMap<>();

    public void RedisReadWriteLock() {
        String password = "123456";
        String host = "localhost";
        String port = "6239";

        Config config = new Config();
        /*Redis单机模式配置*/
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        /*Redis主从模式配置*/
        //config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});

        config.setLockWatchdogTimeout(20000L); //防止死锁设置锁的超时时间（默认30S）
        redissonClient = Redisson.create(config);
    }

    private void createLock(Long serialNumber) {
        //RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.valueOf(serialNumber));

        ReadWriteLock lock = new ReentrantReadWriteLock();
        LOCK_MAP.put(serialNumber, lock);
    }

    public void releaseLock(Long serialNumber) {
        LOCK_MAP.remove(serialNumber);
    }

    public Lock getReadLock(Long serialNumber) {
        ReadWriteLock Lock = LOCK_MAP.get(serialNumber);
        if (Lock == null) {
            createLock(serialNumber);
            Lock = LOCK_MAP.get(serialNumber);
            return Lock.readLock();
        }

        return Lock.readLock();
    }

    public Lock getWriteLock(Long serialNumber) {
        ReadWriteLock Lock = LOCK_MAP.get(serialNumber);
        if (Lock == null) {
            createLock(serialNumber);
            Lock = LOCK_MAP.get(serialNumber);
            return Lock.writeLock();
        }

        return Lock.writeLock();
    }

}
