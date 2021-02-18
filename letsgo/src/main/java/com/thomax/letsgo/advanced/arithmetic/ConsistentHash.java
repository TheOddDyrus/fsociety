package com.thomax.letsgo.advanced.arithmetic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 考虑到分布式系统每个节点都有可能失效，并且新的节点很可能动态的增加进来，如何保证当系统的节点数目发生变化时仍然能够对外提供良好的服务，
 * 这是值得考虑的，尤其实在设计分布式缓存系统时，如果某台服务器失效，对于整个系统来说如果不采用合适的算法来保证一致性，
 * 那么缓存于系统中的所有数据都可能会失效（即由于系统节点数目变少，客户端在请求某一对象时需要重新计算其hash值（通常与系统中的节点数目有关），
 * 由于hash值已经改变，所以很可能找不到保存该对象的服务器节点），因此一致性hash就显得至关重要。
 *
 >>>良好的分布式cahce系统中的一致性hash算法应该满足以下几个方面：
 * 平衡性(Balance)
 * 平衡性是指哈希的结果能够尽可能分布到所有的缓冲中去，这样可以使得所有的缓冲空间都得到利用。很多哈希算法都能够满足这一条件。
 *
 * 单调性(Monotonicity)
 * 单调性是指如果已经有一些内容通过哈希分派到了相应的缓冲中，又有新的缓冲区加入到系统中，那么哈希的结果应能够保证原有已分配的内容可以被映射到新的缓冲区中去，
 * 而不会被映射到旧的缓冲集合中的其他缓冲区。简单的哈希算法往往不能满足单调性的要求，如最简单的线性哈希：x = (ax + b) mod (P)，在上式中，
 * P表示全部缓冲的大小。不难看出，当缓冲大小发生变化时(从P1到P2)，原来所有的哈希结果均会发生变化，从而不满足单调性的要求。
 * 哈希结果的变化意味着当缓冲空间发生变化时，所有的映射关系需要在系统内全部更新。而在P2P系统内，缓冲的变化等价于Peer加入或退出系统，
 * 这一情况在P2P系统中会频繁发生，因此会带来极大计算和传输负荷。单调性就是要求哈希算法能够应对这种情况。
 *
 * 分散性(Spread)
 * 在分布式环境中，终端有可能看不到所有的缓冲，而是只能看到其中的一部分。当终端希望通过哈希过程将内容映射到缓冲上时，由于不同终端所见的缓冲范围有可能不同，
 * 从而导致哈希的结果不一致，最终的结果是相同的内容被不同的终端映射到不同的缓冲区中。这种情况显然是应该避免的，因为它导致相同内容被存储到不同缓冲中去，
 * 降低了系统存储的效率。分散性的定义就是上述情况发生的严重程度。好的哈希算法应能够尽量避免不一致的情况发生，也就是尽量降低分散性。
 *
 * 负载(Load)
 * 负载问题实际上是从另一个角度看待分散性问题。既然不同的终端可能将相同的内容映射到不同的缓冲区中，那么对于一个特定的缓冲区而言，也可能被不同的用户映射为不同的内容。
 * 与分散性一样，这种情况也是应当避免的，因此好的哈希算法应能够尽量降低缓冲的负荷。
 *
 * 平滑性(Smoothness)
 * 平滑性是指缓存服务器的数目平滑改变和缓存对象的平滑改变是一致的
 */
public class ConsistentHash<T> {

    private final HashFunction hashFunction;

    private final int numberOfReplicas;// 节点的复制因子,实际节点个数 * numberOfReplicas = 虚拟节点个数

    private final SortedMap<Long, T> circle = new TreeMap<>();// 存储虚拟节点的hash值到真实节点的映射

    /*简易一致性哈希*/
    public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) {
        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;
        for (T node : nodes) {
            add(node);
        }
    }

    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++)
            /* 对于一个实际机器节点 node, 对应 numberOfReplicas 个虚拟节点
             *
             * 不同的虚拟节点(i不同)有不同的hash值,但都对应同一个实际机器node
             * 虚拟node一般是均衡分布在环上的,数据存储在顺时针方向的虚拟node上
             */
            circle.put(hashFunction.hash(node.toString() + i), node);
    }

    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++)
            circle.remove(hashFunction.hash(node.toString() + i));
    }

    /*
     * 获得一个最近的顺时针节点,根据给定的key 取Hash
     * 然后再取得顺时针方向上最近的一个虚拟节点对应的实际节点
     * 再从实际节点中取得 数据
     */
    public T get(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
        long hash = hashFunction.hash((String) key);
        if (!circle.containsKey(hash)) { //数据映射在两台虚拟机器所在环之间,就需要按顺时针方向寻找机器
            SortedMap<Long, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    public long getSize() {
        return circle.size();
    }

    public SortedMap<Long, T> getCircle() {
        return circle;
    }

    public static void main(String[] args) {
        Set<String> nodes = new HashSet<>();
        nodes.add("A");
        nodes.add("B");
        nodes.add("C");

        ConsistentHash<String> consistentHash = new ConsistentHash<>(new HashFunction(), 2, nodes);
        consistentHash.add("D");

        System.out.println("哈希环尺寸: " + consistentHash.getSize());
        System.out.println("每个节点的哈希值为: ");
        SortedMap<Long, String> circle = consistentHash.getCircle();
        Set<Long> sets = circle.keySet();
        SortedSet<Long> sortedSets = new TreeSet<>(sets);
        for (Long hashCode : sortedSets) {
            System.out.println(hashCode);
        }
        System.out.println("相邻节点的哈希值的差值为: ");
        long keyPre, keyAfter = 0L;
        Iterator<Long> iterator = sortedSets.iterator();
        if (iterator.hasNext()) {
            keyAfter = iterator.next();
        }
        while (iterator.hasNext()) {
            keyPre = keyAfter;
            keyAfter = iterator.next();
            System.out.println(keyAfter - keyPre);
        }
    }

    /**
     * 采用了MD5算法，主要是用来保证平衡性，即能够将机器均衡地映射到环上。貌似用Jdk中String类的hashCode并不能很好的保证平衡性
     */
    private static class HashFunction {
        private MessageDigest md5 = null;

        public long hash(String key) {
            if (md5 == null) {
                try {
                    md5 = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    throw new IllegalStateException("no md5 algrithm found");
                }
            }

            md5.reset();
            md5.update(key.getBytes());
            byte[] bKey = md5.digest();
            //具体的哈希函数实现细节--每个字节 & 0xFF 再移位
            long result = ((long) (bKey[3] & 0xFF) << 24)
                    | ((long) (bKey[2] & 0xFF) << 16
                    | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF));
            return result & 0xffffffffL;
        }
    }

}
