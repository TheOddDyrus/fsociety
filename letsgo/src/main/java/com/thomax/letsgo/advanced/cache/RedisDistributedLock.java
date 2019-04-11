package com.thomax.letsgo.advanced.cache;

import com.thomax.letsgo.advanced.concurrent.LaunderThrowable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.util.ArrayUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Redis实现的分布式锁
 */
public class RedisDistributedLock {

    private JedisPool jedisPool;
    private boolean flag = true;

    public RedisDistributedLock() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200); //假设tomcat设置线程总数+50
        config.setMaxIdle(8);
        config.setMaxWaitMillis(1000 * 100);
        config.setTestOnBorrow(true);
        this.jedisPool = new JedisPool(config, "127.0.0.1", 6379, 3000, "123456", 0);
    }

    /**
     * 加锁
     * @param locaName 锁的key
     * @param acquireTimeout 获取超时时间（毫秒）
     * @param lockExpire 锁的超时时间（秒）
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
     * @param lockName 锁的key
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

    private RedisDistributedLock lock = new RedisDistributedLock();

    public SeckillServer(int total) {
        this.total = total;
    }

    public boolean order() {
        if (total == 0) { //防止多次调用
            System.out.println("秒杀结束！");
            return false;
        }
        String indentifier = lock.lockWithTimeout("resourceX", 5000, 1);
        if (total == 0) { //防止获得锁后total已经没有数量了
            System.out.println("秒杀结束**！");
            return false;
        }
        if (StringUtils.isNotEmpty(indentifier)) {
            System.out.println(Thread.currentThread().getName() + "获得了锁");
            System.out.println(--total); //秒杀剩余数，分布式锁保证了total是线程安全的
            try {
                Thread.sleep(100); //模拟业务
            } catch (InterruptedException e) {
                //
            }
            lock.releaseLock("resourceX", indentifier);
        } else {
            order();
        }
        return true;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(150); //假设tomcat分配最大150个线程
        SeckillServer seckillServer = new SeckillServer(500); //设置秒杀服务的初始值=500
        List<Future<Boolean>> list = new Vector<>();
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);

        Thread thread = new Thread(() -> { //开启一个线程去监听异步结果，如果秒杀结束则停止模拟接口发起的作业
            while (true) {
                int size = list.size();
                System.out.println("++++++++++++++++++++++++++++++++++++++++++size=" + size);
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        Future<Boolean> futureX = list.get(i);
                        try {
                            if (futureX.isDone()) {
                                if(!futureX.get()) {
                                    atomicBoolean.set(false); //终止秒杀服务
                                    return;
                                } else {
                                    list.remove(futureX);
                                    break;
                                }
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            //
                        }
                    }
                }
            }
        });
        thread.start();

        /*模拟秒杀服务*/
        while (atomicBoolean.get()) {
            Future<Boolean> future = executorService.submit(seckillServer::order); //模拟每个接口在秒杀时的作业情况（获得锁 -> 中间业务 -> 释放锁）
            list.add(future);
        }

        while (list.size() == 0) {
            executorService.shutdownNow();
            seckillServer.lock.closeJedis();
        }
    }
}