package com.thomax.letsgo.design.pattern.create;

/**单例模式:
 *	1、某些类创建比较频繁，对于一些大型的对象，这是一笔很大的系统开销。
 *	2、省去了new操作符，降低了系统内存的使用频率，减轻GC压力。
 *	3、有些类如交易所的核心交易引擎，控制着交易流程，如果该类可以创建多个的话，系统完全乱了
 */
public class Singleton {
	public static void main(String[] args) {
		//单例模式的使用
		OddOne.getInstance();
	}
}

//懒汉模式
class OddOne {
	private static OddOne oo = null; //此处赋值为null，目的是实现延迟加载

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
