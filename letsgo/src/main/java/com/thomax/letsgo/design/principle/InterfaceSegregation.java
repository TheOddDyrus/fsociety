package com.thomax.letsgo.design.principle;

/**接口隔离原则：使用多个隔离的接口来降低耦合度
 *	>使用多个专门的接口比使用单一的总接口要好。
 * 	>一个类对另外一个类的依赖性应当是建立在最小的接口上的。
 *	>一个接口代表一个角色，不应当将不同的角色都交给一个接口。没有关系的接口合并在一起会形成一个臃肿的大接口，这是对角色和接口的污染。
 */
public class InterfaceSegregation {

	public static void main(String[] args) {
		//只具备查询的对象
		IOrderForPortal ifp = Order.getOrderForPortal();
		ifp.getOrder();
		//只具备查询和增加的对象
		IOrderForOtherSys ifos = Order.getOrderForOtherSys();
		ifos.insertOrder();
		ifos.getOrder();
		//具备增删改查的对象
		IOrderForAdmin ifa = Order.getOrderForAdmin();
		ifa.getOrder();
		ifa.deleteOrder();
		ifa.updateOrder();
		ifa.getOrder();
	}

}

//只供查询的接口
interface IOrderForPortal {
    String getOrder();
}

//只供查询和增加的接口
interface IOrderForOtherSys {
    String insertOrder();
    String getOrder();
}

//具备增删改查的接口
interface IOrderForAdmin {
	String insertOrder();
	String deleteOrder();
	String updateOrder();
    String getOrder();
}
/*接口最好不要使用继承，达到低耦合的效果
 * interface IOrderForAdmin extends IOrderForPortal, IOrderForOtherSys {
	String updateOrder();
    String deleteOrder();
   }
 */

class Order implements IOrderForPortal, IOrderForOtherSys, IOrderForAdmin {
	
	private Order() {}
	
	public static IOrderForPortal getOrderForPortal() {
		return (IOrderForPortal)new Order();
	}
	
	public static IOrderForOtherSys getOrderForOtherSys() {
		return (IOrderForOtherSys)new Order();
	}
	
	public static IOrderForAdmin getOrderForAdmin() {
		return (IOrderForAdmin)new Order();
	}
	
	public String insertOrder() {
		return "Execute insertOrder";
	}
	
	public String deleteOrder() {
		return "Execute deleteOrder";
	}
	
	public String updateOrder() {
		return "Execute updateOrder";
	}
	
	public String getOrder() {
		return "Execute getOrder";
	}
}
