package com.thomax.letsgo.advanced.arithmetic;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 手撸LRU
 */
public class LRU<K, V> {

    private final int capacity; //初始容量
    private Node first; //链表的头节点
    private Node last; //链表的尾节点
    private final Map<K, Node> map; //数据源

    public LRU(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
    }

    public V get(K key) {
        Node node = map.get(key);
        if (node == null) {
            return null;
        }

        moveToHead(node);
        return node.val;
    }

    public void put(K key, V value) {
        Node node = map.get(key);

        if (node == null) {
            node = new Node();
            node.key = key;
            node.val = value;

            if(map.size() == capacity) {
                removeLast();
            }

            addToHead(node);
            map.put(key, node);
        } else {
            node.val = value;
            moveToHead(node);
        }
    }

    private void moveToHead(Node node) {
        if (node == first) {
            return;
        } else if (node == last) {
            last.prev.next = null;
            last = last.prev;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        node.prev = first.prev;
        node.next = first;
        first.prev = node;
        first = node;
    }

    private void addToHead(Node node) {
        if (map.isEmpty()) {
            first = node;
            last = node;
        } else {
            node.next = first;
            first.prev = node;
            first = node;
        }
    }

    private void removeLast() {
        map.remove(last.key);
        Node prevNode = last.prev;
        if (prevNode != null) {
            prevNode.next = null;
            last = prevNode;
        }
    }

    //自定义双向链表
    private class Node {
        K key;
        V val;
        Node prev;
        Node next;
    }

}

/**
 * 使用LinkedHashMap来实现的LRU
 */
class LRU2<K, V> extends LinkedHashMap<K, V> {

    private final int capacity; //初始容量

    public LRU2(int capacity) {
        super(capacity, 0.75f, true); //将accessOrder设置为true来开启顺序
        this.capacity = capacity;
    }

    //在LinkedHashMap内此方法默认返回false，不重写无法进行删除操作，当返回true以后会在afterNodeInsertion()中执行removeNode()
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

}