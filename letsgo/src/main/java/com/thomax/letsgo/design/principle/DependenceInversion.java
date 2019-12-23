package com.thomax.letsgo.design.principle;

/**依赖倒置原则: 这个是开闭原则的基础，对接口编程，依赖于抽象而不依赖于具体
 *	>高层次的模块不应该依赖于低层次的模块，他们都应该依赖于抽象。
 *	>抽象不应该依赖于具体实现，具体实现应该依赖于抽象。
 */
public class DependenceInversion {

	public static void main(String[] args) {
		//加载了自动驾驶技术的宝马车
		AutoSystem asBmw = new AutoSystem(new BmwCar());
		asBmw.RunCar();
		asBmw.TurnCar();
		asBmw.StopCar();
		//加载了自动驾驶技术的福特车
		AutoSystem asFord = new AutoSystem(new FordCar());
		asFord.RunCar();
		asFord.TurnCar();
		asFord.StopCar();
		//加载了自动驾驶技术的本田车
		AutoSystem asHonda= new AutoSystem(new HondaCar());
		asHonda.RunCar();
		asHonda.TurnCar();
		asHonda.StopCar();
	}
}

//高层次模块：车类接口，具备具体行为
interface ICar {
	void Run();
	void Turn();
	void Stop();
}

//低层次模块：宝马
class BmwCar implements ICar {
	public void Run() {
		System.out.println("宝马开始启动了");
	}

	public void Turn() {
		System.out.println("宝马开始转弯了");
	}

	public void Stop() {
		System.out.println("宝马开始停车了");
	}
}

//低层次模块：福特
class FordCar implements ICar {

	public void Run() {
		System.out.println("福特开始启动了");
	}

	public void Turn() {
		System.out.println("福特开始转弯了");
	}

	public void Stop() {
		System.out.println("福特开始停车了");
	}
}

//低层次模块：本田
class HondaCar implements ICar {

	public void Run() {
		System.out.println("本田开始启动了");
	}

	public void Turn() {
		System.out.println("本田开始转弯了");
	}

	public void Stop() {
		System.out.println("本田开始停车了");
	}
}

//高层次模块：自动驾驶系统
class AutoSystem {
	private ICar icar;

	public AutoSystem(ICar icar) {
		this.icar = icar;
	}

	public void RunCar() {
		System.out.print("自动驾驶开启!-->");
		icar.Run();
	}

	public void TurnCar() {
		System.out.print("自动驾驶转弯!-->");
		icar.Turn();
	}

	public void StopCar() {
		System.out.print("自动驾驶停止!-->");
		icar.Stop();
	}
}
