package com.thomax.letsgo.advanced.distributed.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Redis实现的分布式锁(这个案例为演示版)，正常实战版如下：
 *   V1版本：没有操作，在分布式系统中会造成同一时间，资源浪费而且很容易出现并发问题
 *   V2版本：加了分布式redis锁，在访问核心方法前，加入redis锁可以阻塞其他线程访问,可以很好的处理并发问题,但是缺陷就是如果机器突然宕机，或者线路波动等，就会造成死锁，一直不释放等问题
 *   V3版本：很好的解决了这个问题v2的问题，就是加入时间对比如果当前时间已经大与释放锁的时间说明已经可以释放这个锁重新在获取锁，setget方法可以把之前的锁去掉在重新获取,旧值在于之前的值比较，
 *          如果无变化说明这个期间没有人获取或者操作这个redis锁，则可以重新获取
 *   V4版本：采用成熟的框架redisson,封装好的方法则可以直接处理，但是waittime记住要这只为0
 */
@Slf4j
public class RedisDistributedLock {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisDistributedLock() {
        //单机模式
        RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration();
        redisStandaloneConfig.setPort(6379);
        redisStandaloneConfig.setHostName("127.0.0.1");
        redisStandaloneConfig.setPassword(RedisPassword.of("123456"));

        //配置连接池
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(2);
        poolConfig.setMaxIdle(8);
        poolConfig.setMaxTotal(150);
        poolConfig.setMaxWaitMillis(60 * 1000);

        //配置Lettuce
        LettucePoolingClientConfiguration lettuceConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(30))
                .shutdownTimeout(Duration.ZERO)
                .poolConfig(poolConfig)
                .build();

        RedisConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfig, lettuceConfig);

        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }

    /**
     * 加锁
     *
     * @param key 需要加锁的key
     * @param expire key的过期时间
     * @param timeUnit key的过期时间的时间单位
     * @param waitSeconds 加锁过程的最大等待时间（单位：秒）
     */
    public boolean lock(String key, long expire, TimeUnit timeUnit, long waitSeconds) {
        String uuid = UUID.randomUUID().toString();

        try {
            long start = System.currentTimeMillis();
            long end = waitSeconds * 1000;

            do {
                if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, uuid, expire, timeUnit))) {
                    return true;
                }
                Thread.sleep(100);
            } while (System.currentTimeMillis() - start < end);
        } catch (Exception e) {
            log.error("lock failed! lockKey:{}", key, e);
        }

        return false;
    }

    /**
     * 释放锁
     */
    public void release(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("release failed! lockKey:{}", key, e);
        }
    }
}

/**
 * 秒杀服务
 */
class SeckillServer {

    private int total;
    private int lockNum = 0;
    private int invalidNum = 0;

    private RedisDistributedLock lock = new RedisDistributedLock();

    public SeckillServer(int total) {
        this.total = total;
    }

    public boolean order() {
        String lockName = "resourceX";
        if (total == 0) { //防止多次调用
            System.out.println("秒杀结束！!无效访问: " + ++invalidNum);
            return false;
        }
        if (!lock.lock(lockName, 5, TimeUnit.SECONDS, 2)) {
            return false;
        }
        if (total == 0) { //防止获得锁后total已经没有数量了
            System.out.println("秒杀结束**冗余锁竞争: " + ++lockNum);
            return false;
        }
        if (StringUtils.isNotEmpty(lockName)) {
            System.out.println(Thread.currentThread().getName() + "获得了锁");
            System.out.println(--total); //秒杀剩余数，分布式锁保证了total是线程安全的
            try {
                Thread.sleep(20); //模拟业务
            } catch (InterruptedException e) {
                //
            }
            lock.release(lockName);
        } else {
            order();
        }
        return true;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(150); //假设tomcat分配最大150个线程
        ExecutorService monitorService = Executors.newSingleThreadExecutor(); //单例监听线程保证监听不会出问题
        SeckillServer seckillServer = new SeckillServer(500); //设置秒杀服务的初始值=500
        List<Future<Boolean>> list = Collections.synchronizedList(new LinkedList<>());
        CountDownLatch monitorLock = new CountDownLatch(1);
        long startTime = System.currentTimeMillis();

        /*单线程去监听异步结果，如果秒杀结束则关闭秒杀业务*/
        monitorService.submit(() -> {
            try {
                monitorLock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                for (Future future : list) {
                    if (future.isDone()) {
                        list.remove(future);
                        break;
                    }
                }
                if (list.size() == 0) {
                    System.out.println("++++++++++++++++监听结束，共耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
                    executorService.shutdown();
                    monitorService.shutdown();
                    return;
                }
            }
        });

        /*模拟秒杀服务，1000个用户任务*/
        for (int i = 0; i < 1000; i++) {
            Future<Boolean> future = executorService.submit(seckillServer::order);
            list.add(future);
        }
        monitorLock.countDown(); //释放监听线程的闭锁
    }
}