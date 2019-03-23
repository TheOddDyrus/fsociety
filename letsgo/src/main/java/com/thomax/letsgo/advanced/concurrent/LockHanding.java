package com.thomax.letsgo.advanced.concurrent;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.junit.runner.notification.RunListener.ThreadSafe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Lock提供了比synchronized更多的功能，性能也更好。但是要注意以下2点：
 * 1.Lock不是Java语言内置的，synchronized是Java语言的关键字，因此是JVM内置特性。而实现了Lock接口的类可以实现同步访问；
 * 2.Lock和synchronized有一点非常大的不同，采用synchronized不需要用户去手动释放锁，当synchronized方法或者synchronized代码块执行完之后，系统会自动让线程释放对锁的占用；
 * 而Lock则必须要用户去手动释放锁，如果没有主动释放锁，就有可能导致出现死锁现象
 * <p>
 * 综述：在一些内置锁无法满足需求的条件下，ReentrantLock可以作为一种高级工具。当需要一些高级功能，比如：可定时、可轮询、可中断的锁获取操作、公平队列、非块结构的锁(非块比如方法和属性)。
 * 否则，还是应该优先使用synchronized
 */
public class LockHanding implements Lock, ReadWriteLock {
    /**
     * Lock接口
     */
    public void lock() { } //主动获取锁，如果没有获取到则阻塞
    public void lockInterruptibly() throws InterruptedException { } //中断没有成功获取锁的线程：如果有多个线程同时调用此方法获取锁，第一个获得到锁的线程会去调用其他线程的interrupt()
    public boolean tryLock() { return false; } //尝试获取锁，获取成功返回true，非阻塞
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException { return false; } //在一个时间段内尝试获取锁，获取到锁以后立刻返回true，超时以后停止获取
    public void unlock() { } //释放锁
    public Condition newCondition() { return null; }
    /**
     * ReadWriteLock接口，适用于频繁读取的数据结构。复杂性稍微会更高，但是可以实现多种操作：
     * 1.释放优先：当写入线程释放锁时，同时存在一个读与一个写的线程，那优先给哪个就是根据场景实现了
     * 2.读线程插队：当读线程持有锁，此时有一个写线程在等待，那接下来新到达的读线程是否插队也是根据场景实现（如果插队过多会导致"线程饥饿"问题，写线程永远获得不到锁）
     */
    public Lock readLock() { return null; } //返回共享锁，进入条件：1.没有其他线程的写锁；2.没有写请求，或者有写请求但调用线程和持有锁的线程是同一个
    public Lock writeLock() { return null; } //返回排他锁：进入条件：1.没有其他线程的读锁，2.没有其他线程的写锁

    /**
     * 可重入读写锁案例，在多读场景下性能比ReentrantLock去实现要好
     */
    class ReadWriteMap<K, V> {
        private final Map<K, V> map;
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private final Lock r = lock.readLock();
        private final Lock w = lock.writeLock();
        public ReadWriteMap(Map<K, V> map) {
            this.map = map;
        }

        public V put(K key, V value) {
            w.lock();
            try {
                return map.put(key, value);
            } finally {
                w.unlock();
            }
        }
        public V get(K key) {
            r.lock();
            try {
                return map.get(key);
            } finally {
                r.unlock();
            }
        }
    }

    /**
     * AQS方法详解：
     * 1.它定义两种资源共享方式：Exclusive（独占，只有一个线程能执行，如ReentrantLock）和Share（共享，多个线程可同时执行，如Semaphore、CountDownLatch）
     * 2.以ReentrantLock为例，state初始化为0，表示未锁定状态。A线程lock()时，会调用tryAcquire()独占该锁并将state+1。此后，其他线程再tryAcquire()时就会失败，
     * 直到A线程unlock()到state=0（即释放锁）为止，其它线程才有机会获取该锁。当然，释放锁之前，A线程自己是可以重复获取此锁的（state会累加），这就是可重入的概念。
     * 但要注意，获取多少次就要释放多么次，这样才能保证state是能回到零态的。
     * 3.再以CountDownLatch以例，任务分为N个子线程去执行，state也初始化为N（注意N要与线程个数一致）。这N个子线程是并行执行的，每个子线程执行完后countDown()一次，
     * state会CAS减1。等到所有子线程都执行完后(即state=0)，会unpark()主调用线程，然后主调用线程就会从await()函数返回，继续后余动作。
     * <p>
     * 一般来说，自定义同步器要么是独占方法，要么是共享方式，他们也只需实现tryAcquire-tryRelease、tryAcquireShared-tryReleaseShared中的一种即可。
     * 但AQS也支持自定义同步器同时实现独占和共享两种方式，如ReentrantReadWriteLock
     */
    class AQS extends AbstractQueuedSynchronizer { }
}

/**
 * Mutex是一个不可重入的互斥锁实现。锁资源（AQS里的state）只有两种状态：0表示未锁定，1表示锁定
 *
 * 同步类在实现时一般都将自定义同步器（Sync）定义为内部类，供自己使用；而同步类自己（Mutex）则实现某个接口，对外服务。当然，接口的实现要直接依赖sync，它们在语义上也存在某种对应关系！！
 * 在Sync里只用实现资源state的获取-释放方式tryAcquire-tryRelelase，至于线程的排队、等待、唤醒等，上层的AQS都已经实现好了，我们不用关心。
 * 除了Mutex，ReentrantLock/CountDownLatch/Semphore这些同步类的实现方式都差不多，不同的地方就在获取-释放资源的方式tryAcquire-tryRelelase
 */
class Mutex implements Lock, java.io.Serializable {
    private static class Sync extends AbstractQueuedSynchronizer { // 自定义同步器
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
        public boolean tryAcquire(int acquires) {
            if (compareAndSetState(0, 1)) { //state为0才设置为1，不可重入！
                setExclusiveOwnerThread(Thread.currentThread()); //设置为当前线程独占资源
                return true;
            }
            return false;
        }
        protected boolean tryRelease(int releases) {
            if (getState() == 0) { //既然来释放，那肯定就是已占有状态了。只是为了保险，多层判断！
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);//释放资源，放弃占有状态
            return true;
        }
    }

    private final Sync sync = new Sync(); //同步类的实现都依赖继承于AQS的自定义同步器

    public void lock() {
        sync.acquire(1); //获取资源，即便等待，直到成功才返回
    }
    public void unlock() {
        sync.release(1); //释放资源
    }
    public boolean tryLock() {
        return sync.tryAcquire(1); //尝试获取资源，要求立即返回
    }
    public boolean isLocked() {
        return sync.isHeldExclusively(); //判断是否锁定状态
    }

    /*这里三个方法暂不实现*/
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException { return false; }
    public void lockInterruptibly() throws InterruptedException { }
    public Condition newCondition() { return null; }
}

/**
 * 对象锁和类锁
 */
@ThreadSafe
class ObjectLock {
    private final ObjectLock objLock = new ObjectLock();

    public void exec() {
        synchronized (objLock) {
            //对象锁只会锁住这个对象实例，因为相同类型对象可以有多个
        }
    }

    public void exec2() {
        synchronized (ObjectLock.class) {
            //类锁作用域为整个类，例如声明在静态方法上的synchronized也是持有的类锁
        }
    }
}

/**
 * synchronized是互斥锁，从继承角度来看也具有可重入性
 */
class OriginalClass {
    public synchronized void exec() {
    }
}

class ReentrantClass extends OriginalClass {
    public synchronized void exec() {
        System.out.println("doing something");
        super.exec(); //如果不具有可重入性当线程在子类方法中调用父类方法，将产生死锁
    }
}

/**
 * ConcurrentHashMap、CopyOnWriteArraySet、CopyOnWriteArrayList是三大类集合中性能较高的线程安全集合
 * Collections集合工具类中通过包装器工厂方法synchronizedXX()来new出自己的一个实现了以待同步对象为互斥锁（mutex）的静态内部类，这也是一种通过封闭机制来让非线程安全类变为线程安全的方式
 */
class SynchronizedUtil<K, V> {
    public Map<K, V> getThreadSafeMap(Map<K, V> map, boolean performance) {
        if (performance) {
            return new ConcurrentHashMap<>(map);//分段锁
        }
        return Collections.synchronizedMap(map);
    }

    public Map<K, V> getThreadSafeSortedMap(Map<K, V> map, boolean performance) {
        if (performance) {
            return new ConcurrentSkipListMap<>(map); //JDK 1.6引入的高性能线程安全排序集合
        }
        return Collections.synchronizedMap(map);
    }

    public Set<V> getThreadSafeSet(Set<V> set, boolean performance) {
        if (performance) {
            return new CopyOnWriteArraySet<>(set); //底层基于CopyOnWriteArrayList实现
        }
        return Collections.synchronizedSet(set);
    }

    public Set<V> getThreadSafeSortedSet(Set<V> set, boolean performance) {
        if (performance) {
            return new ConcurrentSkipListSet<>(set); //JDK 1.6引入的高性能线程安全排序集合
        }
        return Collections.synchronizedSet(set);
    }

    public List<V> getThreadSafeList(List<V> list, boolean performance) {
        if (performance) {
            return new CopyOnWriteArrayList<>(list); //并发访问时查询还是基于底层无锁的数组，当增删改时内置可重入锁实现线程安全，适用于迭代操作远远多于修改操作时
        }
        return Collections.synchronizedList(list);
    }
}

/**
 * 线程安全性的委托：CAS.num是非线程安全的，它可以将线程安全的操作委托给AtomicLong
 */
class CAS {
    static AtomicLong atomicLong;
    static long num = 0;
}

class SafeDelegate {
    private ExecutorService threadPool = Executors.newFixedThreadPool(100);

    public void exec() {
        while (CAS.num < 1000) {
            Runnable runnable = () -> {
                long cas = CAS.atomicLong.get();
                long num = CAS.num;
                /*
                一些逻辑操作，当整个线程的执行时间小于线程上下文切换时间时，CAS的操作效率高
                 */
                num++;
                if (CAS.atomicLong.compareAndSet(cas, cas + 1)) {
                    CAS.num = num;
                }
            };

            threadPool.submit(runnable);
        }
    }
}

/**
 * 使用final来实现一个不可变对象，属性声明或形参声明都可以
 */
@Immutable
class Point {
    private final int x;
    private int y;

    public Point(int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

/**
 * 常见原子操作：
 * 若没有则添加、若相等则移除、若相等则替换
 */
