package com.thomax.letsgo.zoom.handling;

import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ThreadHandling {
	public static void main(String[] args) throws Exception {
//		ThreadInterrupt example = new ThreadInterrupt();
//		example.main(args);
		
		for (int i=0; i<10000; i++) {
			if (i%200 == 0 ) System.out.println("+200+ " + i);
		}
	}
}


/**class1-查看线程是否存活*/  
class TwoThreadAlive extends Thread {
	   public void run() {
	      for (int i = 0; i < 10; i++) {
	         printMsg();
	      }
	   }
	 
	   public void printMsg() {
	      System.out.print("name=" + Thread.currentThread().getName() + " ");  ////获得当前Thread的线程Name
	      System.out.print("ID=" + Thread.currentThread().getId()  + " ");  //获得当前Thread的线程Id
	      System.out.println("priority="+ Thread.currentThread().getPriority());  //获得当前Thread的线程优先级Priority
	   }
	   
	   //获得所有线程Id与Name
	   public static void getAllThread() {
		   ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();  //Thread.getThreadGroup()获得当前所有激活的线程组ThreadGroup
		   int noThreads = currentGroup.activeCount();  //ThreadGroup.activeCount()获得组里面有多少个激活的线程
		   System.out.println("当前激活的线程有 " + noThreads + " 个");
		   Thread[] lstThreads = new Thread[noThreads];
		   currentGroup.enumerate(lstThreads);  //ThreadGroup.enumerate(Thread list[]) 枚举每个Thread到Thread数组
		   for (int i = 0; i < noThreads; i++) {
			   System.out.println("线程号：" + i + " = " + lstThreads[i].getName());
		   }	      
	   }
	 
	   public static void main(String[] args) {
		  Thread firstT = new Thread();
		  System.out.println("自动命名的第一个线程: " + firstT.getName());
		  TwoThreadAlive secondT = new TwoThreadAlive();
		  System.out.println("自动命名的第二个线程: " + secondT.getName());
		  
	      TwoThreadAlive tt = new TwoThreadAlive();
	      tt.setName("Thread-Thomas");  //Thread.setName(String name)
	      System.out.println("before start(), tt.isAlive()=" + tt.isAlive());
	      tt.start();  //print "Thread-Thomas"
	      System.out.println("just after start(), tt.isAlive()=" + tt.isAlive());
	      for (int i = 0; i < 10; i++) {
	         tt.printMsg();  //print "main"
	      }
	      System.out.println("The end of main(), tt.isAlive()=" + tt.isAlive());
	      getAllThread();
	   }
}
//自动命名的第一个线程: Thread-1
//自动命名的第二个线程: Thread-2
//before start(), tt.isAlive()=false
//just after start(), tt.isAlive()=true
//name=main ID=1 priority=5
//name=main ID=1 priority=5
//name=main ID=1 priority=5
//name=main ID=1 priority=5
//name=main ID=1 priority=5
//name=main ID=1 priority=5
//name=main ID=1 priority=5
//name=main ID=1 priority=5
//name=main ID=1 priority=5
//name=main ID=1 priority=5
//The end of main(), tt.isAlive()=true
//name=Thread-Thomas ID=13 priority=5
//name=Thread-Thomas ID=13 priority=5
//name=Thread-Thomas ID=13 priority=5
//name=Thread-Thomas ID=13 priority=5
//name=Thread-Thomas ID=13 priority=5
//name=Thread-Thomas ID=13 priority=5
//name=Thread-Thomas ID=13 priority=5
//name=Thread-Thomas ID=13 priority=5
//name=Thread-Thomas ID=13 priority=5
//name=Thread-Thomas ID=13 priority=5
//当前激活的线程有 2 个
//线程号：0 = main
//线程号：1 = Thread-Thomas


/**class2-状态监测*/ 
class MyThreadTest extends Thread {
	boolean waiting= true;
	boolean ready= false;
	
	MyThreadTest() {
	}
	
	public void run() {
		String thrdName = Thread.currentThread().getName();
		System.out.println(thrdName + " starting.");
		while(waiting) {
			System.out.println("waiting:" + waiting);
			System.out.println("waiting...");
			startWait(); 
		}
		try {
			Thread.sleep(1000);
		}
		catch(Exception exc) {
			System.out.println(thrdName + " interrupted.");
		}
		System.out.println(thrdName + " terminating.");
	}
	synchronized void startWait() {
		try {
			while(!ready) wait();
		}catch(InterruptedException exc) {
			System.out.println("wait() interrupted");
		}
	}
	synchronized void notice() {
		ready = true;
		notify();
	}
	
}

class UseMyThreadTest{
	public static void main(String args[]) throws Exception {
		MyThreadTest thrd = new MyThreadTest();
		thrd.setName("MyThread-#1");
		showThreadStatus(thrd);
		
		thrd.start();
		Thread.sleep(50);
		showThreadStatus(thrd);
		
		thrd.waiting = false;
		Thread.sleep(50);
		showThreadStatus(thrd);
		
		thrd.notice();
		Thread.sleep(50);
		showThreadStatus(thrd);
		
		while(thrd.isAlive()) {
			System.out.println("alive");
		}
		showThreadStatus(thrd);
	}
	
	static void showThreadStatus(Thread thrd) {
      	System.out.println(thrd.getName() + " Alive:=" + thrd.isAlive() + " State:=" + thrd.getState()); //Thread.isAlive()线程是否激活 ， Thread.getState()线程状态
      	/*Java中的线程的生命周期大体可分为5种状态。
			1. 新建状态（New）：新创建了一个线程对象。
			2. 就绪状态（Runnable）：线程对象创建后，其他线程调用了该对象的start()方法。该状态的线程位于可运行线程池中，变得可运行，等待获取CPU的使用权。
			3. 运行状态（Running）：就绪状态的线程获取了CPU，执行程序代码。
			4. 阻塞状态（Blocked）：阻塞状态是线程因为某种原因放弃CPU使用权，暂时停止运行。直到线程进入就绪状态，才有机会转到运行状态。阻塞的情况分三种：
				(1) 等待阻塞：运行的线程执行wait()方法，JVM会把该线程放入等待池中。
				(2) 同步阻塞：运行的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线程放入锁池中。
				(3) 其他阻塞：运行的线程执行sleep()或join()方法，或者发出了I/O请求时，JVM会把该线程置为阻塞状态。
					当sleep()状态超时、join()等待线程终止或者超时、或者I/O处理完毕时，线程重新转入就绪状态。
			5. 死亡状态（Dead）：线程执行完了或者因异常退出了run()方法，该线程结束生命周期
      	 */
	}
}
//MyThread-#1 Alive:=false State:=NEW
//MyThread-#1 starting.
//waiting:true
//waiting...
//MyThread-#1 Alive:=true State:=WAITING
//MyThread-#1 Alive:=true State:=WAITING
//MyThread-#1 Alive:=true State:=TIMED_WAITING
//alive
//alive
//alive
//alive
//alive
//alive
//alive
//alive
//alive
//alive
//alive
//alive
//alive
//MyThread-#1 terminating.
//MyThread-#1 Alive:=false State:=TERMINATED


/**class3-线程优先级设置*/
class SimplePriorities extends Thread {
	   private int countDown = 5;
	   private volatile double d = 0; 
	   public SimplePriorities() {
		   //
	   }
	   public SimplePriorities(int priority) {
	      setPriority(priority);  //Thread.setPriority(1-10) 设置线程优先级
	      start();
	   }
	   public String toString() {
	      return super.toString() + ": " + countDown;
	   }
	   public void run() {
	      while(true) {
	         for(int i = 1; i < 100000; i++)
	         d = d + (Math.PI + Math.E) / (double)i;
	         System.out.println(this);
	         if(--countDown == 0) return;
	      }
	   }
	   public static void main(String[] args) {
		  System.out.println("Frist-----------------------");
	      new SimplePriorities(Thread.MAX_PRIORITY);  //MAX_PRIORITY = 10;
	      System.out.println("Second-----------------------");
	      for(int i = 0; i < 5; i++)
	      new SimplePriorities(Thread.MIN_PRIORITY);  //MIN_PRIORITY = 1;
	   }
}
//Frist-----------------------
//Thread[Thread-1,10,main]: 5
//Thread[Thread-1,10,main]: 4
//Thread[Thread-1,10,main]: 3
//Thread[Thread-1,10,main]: 2
//Thread[Thread-1,10,main]: 1
//Second-----------------------
//Thread[Thread-2,1,main]: 5
//Thread[Thread-2,1,main]: 4
//Thread[Thread-2,1,main]: 3
//Thread[Thread-2,1,main]: 2
//Thread[Thread-2,1,main]: 1
//Thread[Thread-3,1,main]: 5
//Thread[Thread-3,1,main]: 4
//Thread[Thread-3,1,main]: 3
//Thread[Thread-3,1,main]: 2
//Thread[Thread-3,1,main]: 1
//Thread[Thread-4,1,main]: 5
//Thread[Thread-4,1,main]: 4
//Thread[Thread-4,1,main]: 3
//Thread[Thread-4,1,main]: 2
//Thread[Thread-4,1,main]: 1
//Thread[Thread-5,1,main]: 5
//Thread[Thread-5,1,main]: 4
//Thread[Thread-5,1,main]: 3
//Thread[Thread-5,1,main]: 2
//Thread[Thread-5,1,main]: 1
//Thread[Thread-6,1,main]: 5
//Thread[Thread-6,1,main]: 4
//Thread[Thread-6,1,main]: 3
//Thread[Thread-6,1,main]: 2
//Thread[Thread-6,1,main]: 1


/**class4-死锁及解决方法*/
/*java 死锁产生的四个必要条件：
	1、互斥使用，即当资源被一个线程使用(占有)时，别的线程不能使用
	2、不可抢占，资源请求者不能强制从资源占有者手中夺取资源，资源只能由资源占有者主动释放。
	3、请求和保持，即当资源请求者在请求其他的资源的同时保持对原有资源的占有。
	4、循环等待，即存在一个等待队列：P1占有P2的资源，P2占有P3的资源，P3占有P1的资源。这样就形成了一个等待环路。
 */
class LockTest {
	   public static String obj1 = "obj1";
	   public static String obj2 = "obj2";
	   public static void main(String[] args) {
	      LockA la = new LockA();
	      new Thread(la).start();
	      LockB lb = new LockB();
	      new Thread(lb).start();
	   }
}

class LockA implements Runnable {
	public void run() {
		try {
			System.out.println(new Date().toString() + " LockA 开始执行");
			while (true) {
				synchronized (LockTest.obj1) {
					LockTest.obj1 = "LockA_obj1";
					System.out.println(new Date().toString() + " LockA 锁住 " + LockTest.obj1);
					Thread.sleep(3000); // 此处等待是给B能锁住机会
					synchronized (LockTest.obj2) {
						LockTest.obj2 = "LockA_obj2";
						System.out.println(new Date().toString() + " LockA 锁住 " + LockTest.obj2);
						Thread.sleep(60 * 1000); // 为测试，占用了就不放
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class LockB implements Runnable {  //
	   public void run() {
	      try {
	         System.out.println(new Date().toString() + " LockB 开始执行");
	         while(true){
	            synchronized (LockTest.obj2) {
	               LockTest.obj2 = "LockB_obj2";
	               System.out.println(new Date().toString() + " LockB 锁住 " + LockTest.obj2);
	               Thread.sleep(3000); // 此处等待是给A能锁住机会
	               synchronized (LockTest.obj1) {
	            	  LockTest.obj1 = "LockB_obj1";
	                  System.out.println(new Date().toString() + " LockB 锁住 " + LockTest.obj1);
	                  Thread.sleep(60 * 1000); // 为测试，占用了就不放
	               }
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }
}
//Sun Mar 18 15:50:23 CST 2018 LockA 开始执行
//Sun Mar 18 15:50:23 CST 2018 LockA 锁住 LockA_obj1
//Sun Mar 18 15:50:23 CST 2018 LockB 开始执行
//Sun Mar 18 15:50:23 CST 2018 LockB 锁住 LockB_obj2
//Sun Mar 18 15:50:26 CST 2018 LockA 锁住 LockA_obj2
//Sun Mar 18 15:50:26 CST 2018 LockB 锁住 LockB_obj1
//Sun Mar 18 15:51:26 CST 2018 LockA 锁住 LockA_obj1
//Sun Mar 18 15:51:26 CST 2018 LockB 锁住 LockB_obj2
//Sun Mar 18 15:51:29 CST 2018 LockA 锁住 LockA_obj2
//Sun Mar 18 15:51:29 CST 2018 LockB 锁住 LockB_obj1


/**class4-死锁及解决方法2*/  //java.util.concurrent.Semaphore; java.util.concurrent.TimeUnit;
/*为了解决这个问题，我们不使用显示的去锁，我们用信号量去控制。
  信号量可以控制资源能被多少线程访问，这里我们指定只能被一个线程访问，就做到了类似锁住。而信号量可以指定去获取的超时时间，我们可以根据这个超时时间，去做一个额外处理。
  对于无法成功获取的情况，一般就是重复尝试，或指定尝试的次数，也可以马上退出。
 */
class UnLockTest {  
	   public static String obj1 = "obj1";
	   public static final Semaphore a1 = new Semaphore(1);
	   public static String obj2 = "obj2";
	   public static final Semaphore a2 = new Semaphore(1);
	 
	   public static void main(String[] args) {
	      LockAa la = new LockAa();
	      new Thread(la).start();
	      LockBb lb = new LockBb();
	      new Thread(lb).start();
	   }
}

class LockAa implements Runnable {
	   public void run() {
	      try {
	         System.out.println(new Date().toString() + " LockA 开始执行");
	         while (true) {
	            if (UnLockTest.a1.tryAcquire(1, TimeUnit.SECONDS)) {
	               System.out.println(new Date().toString() + " LockA 锁住 obj1");
	               if (UnLockTest.a2.tryAcquire(1, TimeUnit.SECONDS)) {
	                  System.out.println(new Date().toString() + " LockA 锁住 obj2");
	                  Thread.sleep(60 * 1000); // do something
	               }else{
	                  System.out.println(new Date().toString() + " LockA 锁 obj2 失败");
	               }
	            }else{
	               System.out.println(new Date().toString() + " LockA 锁 obj1 失败");
	            }
	            UnLockTest.a1.release(); // 释放
	            UnLockTest.a2.release();
	            Thread.sleep(1000); // 马上进行尝试，现实情况下do something是不确定的
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }
}

class LockBb implements Runnable {
	   public void run() {
	      try {
	         System.out.println(new Date().toString() + " LockB 开始执行");
	         while (true) {
	            if (UnLockTest.a2.tryAcquire(1, TimeUnit.SECONDS)) {
	               System.out.println(new Date().toString() + " LockB 锁住 obj2");
	               if (UnLockTest.a1.tryAcquire(1, TimeUnit.SECONDS)) {
	                  System.out.println(new Date().toString() + " LockB 锁住 obj1");
	                  Thread.sleep(60 * 1000); // do something
	               }else{
	                  System.out.println(new Date().toString() + " LockB 锁 obj1 失败");
	               }
	            }else{
	               System.out.println(new Date().toString() + " LockB 锁 obj2 失败");
	            }
	            UnLockTest.a1.release(); // 释放
	            UnLockTest.a2.release();
	            Thread.sleep(10 * 1000); // 这里只是为了演示，所以tryAcquire只用1秒，而且B要给A让出能执行的时间，否则两个永远是死锁
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }
}
//Sun Mar 18 15:57:24 CST 2018 LockB 开始执行
//Sun Mar 18 15:57:24 CST 2018 LockA 开始执行
//Sun Mar 18 15:57:24 CST 2018 LockB 锁住 obj2
//Sun Mar 18 15:57:24 CST 2018 LockA 锁住 obj1
//Sun Mar 18 15:57:25 CST 2018 LockA 锁 obj2 失败
//Sun Mar 18 15:57:25 CST 2018 LockB 锁 obj1 失败
//Sun Mar 18 15:57:26 CST 2018 LockA 锁住 obj1
//Sun Mar 18 15:57:26 CST 2018 LockA 锁住 obj2


/**class5-终止线程*/
class ThreadInterrupt extends Thread
{ 
    public void run() 
    { 
        try 
        { 
            sleep(50000);  // 延迟50秒 
        } 
        catch (InterruptedException e)
        { 
            System.out.println(e.getMessage());
        } 
    } 
    public static void main(String[] args) throws Exception
    { 
        Thread thread = new ThreadInterrupt();
        thread.start(); 
        System.out.println("thread.isInterrupted() -> " + thread.isInterrupted());
        System.out.println("在50秒之内按任意键中断线程!");
        System.in.read();
        thread.interrupt();  //终止线程
        System.out.println("thread.isInterrupted() -> " + thread.isInterrupted());
        thread.join();  //挂起线程
        System.out.println("线程已经退出!");
    } 
}
//thread.isInterrupted() -> false
//在50秒之内按任意键中断线程!
//
//thread.isInterrupted() -> true
//sleep interrupted
//线程已经退出!


/**class6-生产者&消费者问题*/
class ProducerConsumerTest {
	   public static void main(String[] args) {
	      CubbyHole c = new CubbyHole();
	      Producer p1 = new Producer(c, 1);
	      Consumer c1 = new Consumer(c, 1);
	      p1.start(); 
	      c1.start();
	   }
}

class CubbyHole {
	   private int contents;
	   private boolean available = false;
	   public synchronized int get() {  //你希望上锁的Object就应该被synchronized,永远在synchronized的函数或对象里使用wait、notify和notifyAll，不然Java虚拟机会生成IllegalMonitorStateException
	      while (available == false) {
	         try {
	            wait(); //让一个线程暂停运行
	         }
	         catch (InterruptedException e) {
	         }
	      }
	      available = false;
	      notifyAll(); //唤醒所有在等待的线程
	      return contents;
	   }
	   public synchronized void put(int value) {  //你希望上锁的Object就应该被synchronized,永远在synchronized的函数或对象里使用wait、notify和notifyAll，不然Java虚拟机会生成IllegalMonitorStateException
	      while (available == true) {
	         try {
	            wait(); //让一个线程暂停运行
	         }
	         catch (InterruptedException e) {
	         } 
	      }
	      contents = value;
	      available = true;
	      notifyAll(); //唤醒所有在等待的线程
	   }
}

class Producer extends Thread {
	   private CubbyHole cubbyhole;
	   private int number;
	   public Producer(CubbyHole c, int number) {
	      cubbyhole = c;
	      this.number = number;
	   }
	   public void run() {
	      for (int i = 0; i < 10; i++) {
	         cubbyhole.put(i);
	         System.out.println("生产者#" + this.number + " put: " + i);
	         try {
	            sleep((int)(Math.random() * 100));
	         } catch (InterruptedException e) { }
	      }
	   }
}

class Consumer extends Thread {
	   private CubbyHole cubbyhole;
	   private int number;
	   public Consumer(CubbyHole c, int number) {
	      cubbyhole = c;
	      this.number = number;
	   }
	   public void run() {
	      int value = 0;
	         for (int i = 0; i < 10; i++) {
	            value = cubbyhole.get();
	            System.out.println("消费者#" + this.number+ " got: " + value);
	         }
   	   }
}
//生产者#1 put: 0
//消费者#1 got: 0
//消费者#1 got: 1
//生产者#1 put: 1
//消费者#1 got: 2
//生产者#1 put: 2
//生产者#1 put: 3
//消费者#1 got: 3
//生产者#1 put: 4
//消费者#1 got: 4
//消费者#1 got: 5
//生产者#1 put: 5
//生产者#1 put: 6
//消费者#1 got: 6
//生产者#1 put: 7
//消费者#1 got: 7
//生产者#1 put: 8
//消费者#1 got: 8
//消费者#1 got: 9
//生产者#1 put: 9





































