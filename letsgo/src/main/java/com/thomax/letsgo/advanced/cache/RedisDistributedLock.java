package com.thomax.letsgo.advanced.cache;

import com.thomax.letsgo.advanced.concurrent.LaunderThrowable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.util.ArrayUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
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
                    Thread.currentThread().interrupt();
                }
            }
        } catch (JedisException e) {
            e.printStackTrace();
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
        if (jedisPool != null) {
            jedisPool.close();
        }
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
            //lock.closeJedis();
            return false;
        }
        String indentifier = lock.lockWithTimeout("resourceX", 5000, 1);
        System.out.println(Thread.currentThread().getName() + "获得了锁");
        if (total == 0) { //防止获得锁后total已经没有数量了
            System.out.println("秒杀结束！");
            //lock.closeJedis();
            return false;
        }
        System.out.println(--total); //秒杀剩余数，分布式锁保证了total是线程安全的
        for (int j, i = 1; i < Integer.MAX_VALUE; i++) {
            j = 1234256789 / i; //模拟500毫秒业务耗时
        }
        return lock.releaseLock("resourceX", indentifier);
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(150); //假设tomcat分配最大150个线程
        SeckillServer seckillServer = new SeckillServer(500); //设置秒杀服务的初始值=500
        Queue<Future<Boolean>> queue = new LinkedBlockingQueue<>();
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean monitor = true;
        /*模拟秒杀服务*/
        while (atomicBoolean.get()) {
            Future<Boolean> future = executorService.submit(seckillServer::order);//模拟每个接口在秒杀时的作业情况（获得锁 -> 中间业务 -> 释放锁）
            queue.offer(future);

            if (monitor) { //开启一个线程去监听异步结果，如果秒杀结束则停止模拟接口发起的作业
                Thread thread = new Thread(() -> {
                    while (true) {
                        Future<Boolean> futureX = queue.poll();
                        try {
                            if (futureX != null && !futureX.get()) {
                                atomicBoolean.set(false); //终止秒杀服务
                                return;
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                });
                monitor = false;
                thread.start();
            }
            /*try {
                if (!future.get()) {
                    break; //结束秒杀服务
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); //说明还未返回结果，设置当前线程为中断状态，等待下一次被CPU调度
            } catch (ExecutionException e) {
                throw LaunderThrowable.handle(e);
            }*/
        }
        executorService.shutdownNow();
    }
}