package com.thomax.letsgo.design.pattern.create;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**单例模式:
 *	1、某些类创建比较频繁，对于一些大型的对象，这是一笔很大的系统开销。
 *	2、省去了new操作符，降低了系统内存的使用频率，减轻GC压力。
 *	3、有些类如交易所的核心交易引擎，控制着交易流程，如果该类可以创建多个的话，系统完全乱了
 */
public class Singleton {
	public static void main(String[] args) throws InterruptedException {
		ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<>();
		int nThread = 100;
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(nThread);
		ExecutorService executorService = Executors.newFixedThreadPool(nThread);
		for (int i = 0; i < nThread; i++) {
			executorService.submit(() -> {
				try {
					startGate.await();
					set.add(OddOne.getInstance().hashCode());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					endGate.countDown();
				}
			});
		}

		startGate.countDown();
		endGate.await();
		System.out.println("多线程创建单例总数：" +set.size());
		executorService.shutdown();
	}
}

//懒汉模式
class OddOne {
	private static OddOne oo = null;

	private OddOne() {}
	
	public static OddOne getInstance() {
		if (oo == null) {
			synchronized (OddOne.class) {
				if (oo == null) {
					oo = new OddOne();
				}
			}
		}

		return oo;
	}
}

//饥汉模式
class OddOne2 {
	private static OddOne2 oo = new OddOne2();
	private OddOne2() {}
	public static OddOne2 getInstance() {
		return oo;
	}
}
