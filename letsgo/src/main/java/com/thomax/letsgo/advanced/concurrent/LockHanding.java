package com.thomax.letsgo.advanced.concurrent;

import jdk.nashorn.internal.ir.annotations.Immutable;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;
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
    /**
     * AQS方法详解：
     * 1.它定义两种资源共享方式：Exclusive（独占，只有一个线程能执行，如ReentrantLock）和Share（共享，多个线程可同时执行，如Semaphore、CountDownLatch）
     * 2.以ReentrantLock为例，state初始化为0，表示未锁定状态。A线程lock()时，会调用tryAcquire()独占该锁并将state+1。此后，其他线程再tryAcquire()时就会失败，
     *   直到A线程unlock()到state=0（即释放锁）为止，其它线程才有机会获取该锁。当然，释放锁之前，A线程自己是可以重复获取此锁的（state会累加），这就是可重入的概念。
     *   但要注意，获取多少次就要释放多么次，这样才能保证state是能回到零态的。
     * 3.再以CountDownLatch以例，任务分为N个子线程去执行，state也初始化为N（注意N要与线程个数一致）。这N个子线程是并行执行的，每个子线程执行完后countDown()一次，
     *   state会CAS减1。等到所有子线程都执行完后(即state=0)，会unpark()主调用线程，然后主调用线程就会从await()函数返回，继续后余动作。
     *
     * 一般来说，自定义同步器要么是独占方法，要么是共享方式，他们也只需实现tryAcquire-tryRelease、tryAcquireShared-tryReleaseShared中的一种即可。
     * 但AQS也支持自定义同步器同时实现独占和共享两种方式，如ReentrantReadWriteLock
     */
    class AQS extends AbstractQueuedSynchronizer {
        /*AQS中获取操作和释放操作的标准形式：
        boolean acquire() throws InterruptedException {
            while (当前状态不允许获取操作) {
                if (需要阻塞) {
                    如果当前线程不在队列中，将其插入队列
                    阻塞当前队列
                } else {
                    返回失败
                }
            }
            根据独占方式/共享方式来更新同步器的状态
            如果线程位于队列中，则将其移出队列
            返回成功
        }
        void release() {
            更新同步器的状态
            if (新的状态允许被某个被阻塞的线程获取成功) {
                解除队列中一个或多个线程的阻塞状态
            }
        }
        */
    }
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
 * （常见原子操作：若没有则添加、若相等则移除、若相等则替换）
 */
class CAS {
    static AtomicLong atomicLong = new AtomicLong();
    static long num = 0;
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

    /**
     * AtomicReference与AtomicStampedReference的使用
     */
    class ConcurrentStack<T> {
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
 * volatile关键字的2个主要功能：
 * 1.屏蔽指令重排序（DLC）优化
 *   （指令重排序：指CPU采用了允许将多条指令不按程序规定的顺序分开发送给各个相应的电路单元处理，只有非依赖性指令之间才会重排，最终只要获得一致的结果即可。
 *                比如算数的多元运算可以重排；比如单例设计模式com.thomax.letsgo.design.pattern.create.Singleton中对象创建，当多线程访问时，
 *                如果不加volatile会出现new一个对象以后这个对象还未实例化完成但是对象!=null，致后面线程直接获得未实例化完成的对象）
 * 2.通过反汇编码中的lock可以看出锁了CPU的缓存行，利用缓存一致性协议让CPU多级缓存中的数据失效从而重新去内存里面加载数据
 *
 * =>发展：volatile是在synchronized性能低下的时候提出的。如今在JDK6的synchronized关键字的性能被大幅优化之后，更是几乎没有使用它的场景
 */
class Volatile { }

/**
 * Java内存模型的有序性：
 *   如果在本地线程内观察，所有操作都是有序的（Within-Thread As-If-Serial Semantics）
 *   如果在一个线程观察另外一个线程，所有操作都是无序的，比如 "指令重排序" 和 "工作内存与主内存同步延迟"
 *
 * happens-before先行发生原则：
 *   这个是Java内存模型中定义的两项操作之间的偏序关系，如果说操作A先行发生于操作B，其实就是说在发生操作B之前，操作A产生"影响"能被操作B观察到，
 *   "影响"包括修改了内存中共享的值、发送了消息、调用了方法等。
 */
class Ordering { }














