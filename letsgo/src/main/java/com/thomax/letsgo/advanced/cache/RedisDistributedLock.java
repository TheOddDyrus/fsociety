package com.thomax.letsgo.advanced.cache;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Redis实现的分布式锁(这个案例为演示版)，正常实战版如下：
 *   V1版本：没有操作，在分布式系统中会造成同一时间，资源浪费而且很容易出现并发问题
 *   V2版本：加了分布式redis锁，在访问核心方法前，加入redis锁可以阻塞其他线程访问,可以很好的处理并发问题,但是缺陷就是如果机器突然宕机，或者线路波动等，就会造成死锁，一直不释放等问题
 *   V3版本：很好的解决了这个问题v2的问题，就是加入时间对比如果当前时间已经大与释放锁的时间说明已经可以释放这个锁重新在获取锁，setget方法可以把之前的锁去掉在重新获取,旧值在于之前的值比较，
 *          如果无变化说明这个期间没有人获取或者操作这个redis锁，则可以重新获取
 *   V4版本：采用成熟的框架redisson,封装好的方法则可以直接处理，但是waittime记住要这只为0
 */
public class RedisDistributedLock {

    private JedisPool jedisPool;

    public RedisDistributedLock() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(150);
        config.setMaxIdle(8);
        config.setMaxWaitMillis(1000 * 100);
        config.setTestOnBorrow(true);
        this.jedisPool = new JedisPool(config, "127.0.0.1", 6379, 3000, "123456", 0);
    }

    /**
     * 加锁
     * @param locaName       锁的key
     * @param acquireTimeout 获取超时时间（毫秒）
     * @param lockExpire     锁的超时时间（秒）
     * @return 锁标识
     */
    public String lockWithTimeout(String locaName, long acquireTimeout, int lockExpire) {
        try (Jedis conn = jedisPool.getResource()) {
            String identifier = UUID.randomUUID().toString();
            String lockKey = "lock:" + locaName;
            long end = System.currentTimeMillis() + acquireTimeout;
            while (System.currentTimeMillis() < end) {
                if (conn.setnx(lockKey, identifier) == 1) {
                    conn.expire(lockKey, lockExpire);
                    return identifier;
                }
                if (conn.ttl(lockKey) == -1) { // 返回-1代表key没有设置超时时间，为key设置一个超时时间
                    conn.expire(lockKey, lockExpire);
                }
                try {
                    Thread.sleep(10); //while周期不成功则休眠一下，防止一直提交
                } catch (InterruptedException e) {
                    //
                }
            }
        } catch (JedisException e) {
            return null;
        }
        return null;
    }

    /**
     * 释放锁
     * @param lockName   锁的key
     * @param identifier 释放锁的标识
     */
    public boolean releaseLock(String lockName, String identifier) {
        String lockKey = "lock:" + lockName;
        boolean retFlag = false;
        try (Jedis conn = jedisPool.getResource()) {
            while (true) {
                conn.watch(lockKey); //监视lock，准备开始事务
                if (identifier.equals(conn.get(lockKey))) { //通过前面返回的value值判断是不是该锁，若是该锁，则删除，释放锁
                    Transaction transaction = conn.multi();
                    transaction.del(lockKey);
                    List<Object> results = transaction.exec();
                    if (results == null) {
                        continue;
                    }
                    retFlag = true;
                }
                conn.unwatch();
                break;
            }
        } catch (JedisException e) {
            e.printStackTrace();
        }
        return retFlag;
    }

    public void closeJedis() {
        jedisPool.close();
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
        if (total == 0) { //防止多次调用
            System.out.println("秒杀结束！!无效访问: " + ++invalidNum);
            return false;
        }
        String identifier = lock.lockWithTimeout("resourceX", 5000, 1);
        if (total == 0) { //防止获得锁后total已经没有数量了
            System.out.println("秒杀结束**冗余锁竞争: " + ++lockNum);
            return false;
        }
        if (StringUtils.isNotEmpty(identifier)) {
            System.out.println(Thread.currentThread().getName() + "获得了锁");
            System.out.println(--total); //秒杀剩余数，分布式锁保证了total是线程安全的
            try {
                Thread.sleep(20); //模拟业务
            } catch (InterruptedException e) {
                //
            }
            lock.releaseLock("resourceX", identifier);
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
                    seckillServer.lock.closeJedis();
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