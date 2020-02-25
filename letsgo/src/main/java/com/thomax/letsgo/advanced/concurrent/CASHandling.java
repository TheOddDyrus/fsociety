package com.thomax.letsgo.advanced.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * CAS操作
 */
public class CASHandling {

    /**
     * 线程安全性的委托：CAS.num是非线程安全的，它可以将线程安全的操作委托给AtomicLong
     * （常见原子操作：若没有则添加、若相等则移除、若相等则替换）
     */
    static AtomicLong atomicLong = new AtomicLong();
    static long num = 0;
    private ExecutorService threadPool = Executors.newFixedThreadPool(100);
    public void exec() {
        while (CASHandling.num < 1000) {
            Runnable runnable = () -> {
                long cas = CASHandling.atomicLong.get();
                long num = CASHandling.num;
                /*
                一些逻辑操作，当整个线程的执行时间小于线程上下文切换时间时，CAS的操作效率高
                 */
                num++;
                if (CASHandling.atomicLong.compareAndSet(cas, cas + 1)) {
                    CASHandling.num = num;
                }
            };

            threadPool.submit(runnable);
        }
    }

    /**
     * AtomicReference与AtomicStampedReference的使用
     */
    class ConcurrentStack<T> {
        /**
         * 非阻塞栈的简易实现
         */
        AtomicReference<Node<T>> top = new AtomicReference<>();
        public void push(T item) {
            Node<T> newHead = new Node<>(item);
            Node<T> oldHead;
            do {
                oldHead = top.get();
                newHead.nextNode = oldHead;
            } while (!top.compareAndSet(oldHead, newHead));
        }
        public T pop() {
            Node<T> oldHead;
            Node<T> newHead;
            do {
                oldHead = top.get();
                if (oldHead == null) {
                    return null;
                }
                newHead = oldHead.nextNode;
            } while (!top.compareAndSet(oldHead, newHead));

            return oldHead.item;
        }

        /**
         * 非阻塞链表的简易实现
         */
        private final Node<T> dummy = new Node<>(null, null);
        private final AtomicReference<Node<T>> tail = new AtomicReference<>(dummy);
        private AtomicReferenceFieldUpdater<Node, Node> nextUpdater = AtomicReferenceFieldUpdater.newUpdater(Node.class, //通过nextUpdater来更新next域可以减少频繁创建AtomicReference的开销
                Node.class,  //在ConcurrentLinkedQueue中就是使用这种原子的域更新器
                "next");
        public boolean put(T item) {
            Node<T> newNode = new Node<>(item, null);
            while (true) {
                Node<T> curTail = tail.get();
                Node<T> tailNext = curTail.nextReference.get();
                if (curTail == tail.get()) {
                    if (tailNext != null) {
                        tail.compareAndSet(curTail, tailNext);
                    } else {
                        if (curTail.nextReference.compareAndSet(null, newNode)) {
                            tail.compareAndSet(curTail, newNode);
                            return true;
                        }
                    }
                }
            }
        }

        /**
         * ABA问题：
         *  现有一个用单向链表实现的堆栈，栈顶为A，这时线程T1已经知道A.next为B，然后希望用CAS将栈顶替换为B：
         *      original: A -> B     want to: B
         *  在T1执行head.compareAndSet(A,B)之前，线程T2介入，将A、B出栈，再push D、C、A，此时堆栈结构如下图，而对象B此时处于游离状态：
         *      now: A -> C -> D
         *  此时轮到线程T1执行CAS操作，检测发现栈顶仍为A，所以CAS成功，栈顶变为B，但实际上B.next为null，所以此时的情况变为：
         *      now: B
         *  其中堆栈中只有B一个元素，C和D组成的链表不再存在于堆栈中，平白无故就把C、D丢掉了。
         *
         *  ==>各种乐观锁的实现中通常都会使用AtomicStampedReference来避免并发操作带来的问题（如果并发操作的逻辑自己可控制不会出现ABA问题还是可以使用AtomicReference的，例如上面案例ConcurrentStack）
         */
        private final AtomicStampedReference<Node<T>> head = new AtomicStampedReference<>(dummy,0);
        public void exec(T item) {
            Node<T> newHead = new Node<>(item);
            Node<T> reference;
            do {
                reference = head.get(new int[0]); //这个传入参数简直就是毫无作用，内部返回的和getReference()返回的一样
                Node<T> reference2 = head.getReference(); //reference == reference2
                /*一些ABA类型的操作*/
            } while (!head.compareAndSet(reference, newHead, head.getStamp(), head.getStamp() + 1)); //通过Stamp版本来确保对于历史操作的正确性判断
        }

        private class Node<E> {
            public final E item;
            public Node<E> nextNode;
            public final AtomicReference<Node<E>> nextReference;
            public volatile Node<E> next; //这个属性提供给AtomicReferenceFieldUpdater创建来减少每次创建Node对象的开销
            public Node(E item) {
                this.item = item;
                this.nextReference = new AtomicReference<>(); //可以无视这个属性的初始化（追求案例简洁只用一个实体类Node）
            }
            public Node(E item, Node<E> nextNode) {
                this.item = item;
                this.nextReference = new AtomicReference<>(nextNode);
            }
        }
    }

}
