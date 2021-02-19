package com.thomax.letsgo.advanced.concurrent;

import org.junit.runner.notification.RunListener.ThreadSafe;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Lock提供了比synchronized更多的功能，但是要注意以下2点：
 * 1.Lock不是Java语言内置的，实现了Lock接口的类可以实现同步访问；synchronized是Java语言的关键字，因此是JVM内置特性，随着JDK版本提高（1.6及以后）内置CAS以后性能愈发提高。
 * 2.Lock和synchronized有一点非常大的不同，采用synchronized不需要用户去手动释放锁，当synchronized方法或者synchronized代码块执行完之后，系统会自动让线程释放对锁的占用；
 *   而Lock则必须要用户去手动释放锁，如果没有主动释放锁，就有可能导致出现死锁现象。
 *
 * 综述：在synchronized无法满足需求的条件下，ReentrantLock可以作为一种高级工具，比如可定时、可轮询、可中断的锁获取操作、公平队列、非块结构的锁(非块比如方法和属性)。
 *      否则，还是应该优先使用synchronized，在JDK1.6及以后synchronized和ReentrantLock的性能基本完全持平，而且虚拟机在未来性能优化肯定会偏向于原生的synchronized。
 *      相比synchronized，ReentrantLock增加了一些高级功能主要来说主要有三点：
 *          ①等待可中断；
 *          ②可实现公平锁；
 *          ③可实现选择性通知
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
    public Condition newCondition() { //对于每个Lock，可以有多个Condition对象，用它来替代传统的Object的wait()、notify()实现线程间的协作更加安全和高效（这个类位于AQS的内部，基于AQS实现）
        return new Condition() {
            public void await() throws InterruptedException { } //代替Object.wait()，当前线程进入等待状态直到被通知（signal）或者中断
            public void awaitUninterruptibly() { } //当前线程进入等待状态直到被通知，在此过程中对中断信号不敏感，不支持中断当前线程
            public long awaitNanos(long nanosTimeout) throws InterruptedException { return 0; } //当前线程进入等待状态，直到被通知、中断或者超时
            public boolean await(long time, TimeUnit unit) throws InterruptedException { return false; } //当前线程进入等待状态，直到被通知、中断或者超时
            public boolean awaitUntil(Date deadline) throws InterruptedException { return false; } //当前线程进入等待状态，直到被通知、中断或者超时
            public void signal() { } //代替Object.notify()，唤醒一个等待在Condition上的线程，被唤醒的线程在方法返回前必须获得与Condition对象关联的锁
            public void signalAll() { } //代替Object.notifyAll()，唤醒所有等待在Condition上的线程，能够从await()等方法返回的线程必须先获得与Condition对象关联的锁
        };
    }

    /**
     * ReadWriteLock接口
     * 适用于频繁读取的数据结构。复杂性稍微会更高，但是可以实现多种操作：
     * 1.释放优先：当写入线程释放锁时，同时存在一个读与一个写的线程，那优先给哪个就是根据场景实现了
     * 2.读线程插队：当读线程持有锁，此时有一个写线程在等待，那接下来新到达的读线程是否插队也是根据场景实现（如果插队过多会导致"线程饥饿"问题，写线程永远获得不到锁）
     */
    public Lock readLock() { return null; } //返回共享锁，即共享方式，进入条件：1.没有其他线程的写锁；2.没有写请求，或者有写请求但调用线程和持有锁的线程是同一个
    public Lock writeLock() { return null; } //返回排他锁，即独占方法，进入条件：1.没有其他线程的读锁；2.没有其他线程的写锁

}

/**
 * 案例1：可重入读写锁案例，在多读场景下性能比ReentrantLock去实现要好
 */
class ReadWriteMap<K, V> {
    private final Map<K, V> map;
    private final ReadWriteLock lock = new ReentrantReadWriteLock(); //不传参默认为可重入锁中的非公平锁，尝试获取锁的线程有可能会成功，如果不成功的话，则会进入AQS的队列中
    private final Lock r = lock.readLock(); //共享锁，即共享方式
    private final Lock w = lock.writeLock(); //排他锁，即独占方式
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
 * 案例2：一个不可重入的互斥锁实现，AQS里的state只有两种状态：0表示未锁定，1表示锁定
 *
 * 同步类在实现时一般都将自定义同步器（Sync）定义为内部类，供自己使用；而同步类自己（Mutex）则实现某个接口，对外服务。当然，接口的实现要直接依赖Sync，它们在语义上也存在某种对应关系！！
 * 在Sync里只用实现资源state的获取-释放方式tryAcquire-tryRelelase，至于线程的排队、等待、唤醒等，上层的AQS都已经实现好了，我们不用关心。
 * 除了Mutex，ReentrantLock、CountDownLatch、emphore这些同步类的实现方式都差不多，不同的地方就在获取-释放资源的方式tryAcquire-tryRelease
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
 * 案例3：Condition实现一个有界缓存，实现生产者与消费者的阻塞队列关系，其通知关系比用Object.notify()更精确，效率比Object.notifyAll()高
 */
class ConditionBoundedBuffer<T> {
    protected final Lock lock = new ReentrantLock(); //不传参默认为可重入锁中的非公平锁，尝试获取锁的线程有可能会成功，如果不成功的话，则会进入AQS的队列中
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private final T[] items;
    private int tail, head, count;
    public ConditionBoundedBuffer(int size) {
        items = (T[]) new Object[size];
    }

    public void put(T x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await(); //线程被Condition锁住进入阻塞，当被通知以后，所有线程只有一个会被唤醒
            }
            items[tail] = x;
            if (++tail == items.length) {
                tail = 0;
            }
            ++count;
            notEmpty.signal(); //通知唤醒
        } finally {
            lock.unlock();
        }
    }
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await(); //线程被Condition锁住进入阻塞，当被通知以后，所有线程只有一个会被唤醒
            }
            T x = items[head];
            items[head] = null;
            if (++head == items.length) {
                head = 0;
            }
            --count;
            notFull.signal(); //通知唤醒
            return x;
        } finally {
            lock.unlock();
        }
    }
}

/**
 * synchronized的作用域
 */
@ThreadSafe
class ObjectLock {
    private final ObjectLock objLock = new ObjectLock();

    public void exec() {
        synchronized (objLock) {
            //对象锁只会锁住这个对象实例，因为相同类型对象可以有多个
        }
        synchronized (this) {
            //this代表本身这个类的实例对象，作用域也是这个类的所有普通方法，效果和把synchronized加在普通方法上一样，只是锁住的代码面积较小
        }
    }
    public void exec2() {
        synchronized (ObjectLock.class) {
            //类锁作用域为整个类，例如声明在静态方法上的synchronized也是持有的类锁
        }
    }
    public synchronized void exec3() {
        //普通方法上的synchronized只会锁住所有的普通方法
    }
}

/**
 * synchronized是互斥锁，从继承角度来看也具有可重入性
 */
class OriginalClass {
    public synchronized void exec() { }
}
class ReentrantClass extends OriginalClass {
    public synchronized void exec() {
        System.out.println("doing something");
        super.exec(); //如果不具有可重入性当线程在子类方法中调用父类方法，将产生死锁
    }
}

/**
 * Java中的每个对象都有一个监视器(monitor)，来监测并发代码的重入。在非多线程编码时该监视器不发挥作用，
 * 反之如果在synchronized 范围内，监视器发挥作用
 *
 * 当某代码并不持有监视器的使用权时去wait或notify，会抛出java.lang.IllegalMonitorStateException
 */
class SynchronizedMonitor {
    private final Object lock = new Object();

    public void run1() {
        synchronized(lock){ //synchronized块中的方法获取了lock实例的monitor
         //
        }
    }

    public synchronized void run2() { //对比与上面代码中用lock来锁定的效果，实际获取的是SynchronizedMonitor类的monitor
        //
    }

    public static synchronized void run3() { //如果修饰的是static方法，则锁定该类所有实例
        //
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