package com.thomax.letsgo.advanced.cache;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Zookeeper实现的分布式锁：
 * 首先zookeeper创建个PERSISTENT持久节点，然后每个要获得锁的线程都会在这个节点下创建个临时顺序节点，然后规定节点最小的那个获得锁，
 * 所以每个线程首先都会判断自己是不是节点序号最小的那个，如果是则获取锁，如果不是则监听比自己小的上一个节点，如果上一个节点不存在了，
 * 然后会再一次判断自己是不是序号最小的那个节点，是则获得锁，不是重复上述动作。
 *
 * 这里什么要创建临时顺序节点呢，因为要避免羊群效应，所谓羊群效应就是每个节点挂掉，所有节点都去监听，然后做出反映，这样会给服务器带来巨大压力，
 * 所以有了临时顺序节点，当一个节点挂掉只有监听这个节点的才会做出反映。
 *
 */
public class ZookeeperDistributedLock implements Lock {

    private ZkClient zkClient = new ZkClient("127.0.0.1:2181");
    private String beforePath;
    private String currentPath;
    private static String path = "path" + "/";
    private CountDownLatch latch;

    @Override
    public void lock() {
        if (tryLock()) {
            System.out.println("获得了锁！");
        } else {
            waitForLock();
            lock();
        }
    }

    @Override
    public boolean tryLock() {
        if (StringUtils.isBlank(currentPath)) {
            currentPath = zkClient.createEphemeralSequential(path, "lock-");
            List<String> children = zkClient.getChildren(path);
            Collections.sort(children);
            if (currentPath.equals(path + children.get(0))) {
                return true; //如果是第一个节点就获得了锁
            } else {
                int search = Collections.binarySearch(children, currentPath.substring(6));
                beforePath = path + children.get(search - 1);
            }
        }
        return false;
    }

    private void waitForLock() {
        IZkDataListener iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("前面的挂了，执行了...");
                if (latch != null) {
                    latch.countDown();
                }
            }
            @Override
            public void handleDataDeleted(String dataPath) throws Exception { }
        };
        zkClient.subscribeDataChanges(beforePath, iZkDataListener);
        if (zkClient.exists(beforePath)) {
            latch = new CountDownLatch(1);
            latch.countDown();
        }
        zkClient.unsubscribeDataChanges(beforePath, iZkDataListener);
    }

    @Override
    public void unlock() { }
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException { return false; }
    @Override
    public void lockInterruptibly() throws InterruptedException { }
    @Override
    public Condition newCondition() { return null; }

}
