package com.thomax.letsgo.design.principle;

/**合成复用原则:
 *  1.聚合用来表示“拥有”关系或者整体与部分的关系
 *  2.合成用来表示一种强得多的“拥有”关系。例如房子和房间的关系，当房子没了，房间也不可能独立存在
 *  3.依赖是类与类之间的连接，表示一个类依赖于另外一个类的定义。依赖关系仅仅描述了类与类之间的一种使用与被使用的关系，
 *    在Java中体现为局部变量、方法的参数或者是对静态方法的调用
 *	>尽量使用合成/聚合的方式，而不是使用继承。继承实际上破坏了类的封装性，超类的方法可能会被子类修改
 *	>在一个新的对象里面使用一些已有的对象，使之成为新对象的一部分；新的对象通过向这些对象的委派达到复用已有功能的目的。
 */
public class CompositeReuse {

	public static void main(String[] args) {
		Ronaldo ronaldo = new Ronaldo();
		ronaldo.bicycleKick(); //聚合复用
		SonOfRonaldo sonOfRonaldo = new SonOfRonaldo();
		sonOfRonaldo.bicycleKick(); //继承复用
		sonOfRonaldo.basketball();  //新扩展
		House house = new House();
		house.createHouse();  //合成复用
		Person person = new Person();
		person.fishing(); //依赖关系
	}
}

//足球运动员脚(子类)
class Foot {
	public void leftFootDump() {
		System.out.println("左脚发力跃起！");
	}
	
	public void rightFootShot() {
		System.out.println("右脚摆动世界波！");
	}
}

//足球运动员鞋(子类)
class NikeShoe {
	
	private int size;
	
	public NikeShoe(int size) {
		this.size = size;
	}
	
	public void strongShoe() {
		System.out.println("配备强力" + size + "码战靴！");
	}
}

/**1.聚合:
 * 足球巨星Ronaldo(超类)
 */
class Ronaldo {
	
	private Foot foot;
	
	private NikeShoe nikeShoe;
	
	protected int SIZE = 45;
	
	//聚合表示拥有关系或者整体与部分的关系，来创造行为
	public void bicycleKick() {
		nikeShoe = new NikeShoe(SIZE);
		nikeShoe.strongShoe();
		foot = new Foot();
		foot.leftFootDump();
		foot.rightFootShot();
	}
}

//Is-A：继承复用通过子类增加新的属性和方法来扩展超类的实现
class SonOfRonaldo extends Ronaldo {
	
	public SonOfRonaldo() {
		super.SIZE = 30;
	}
	
	public void basketball() {
		System.out.println("我还会打篮球！");
	}
}

//房间(子类)
class Room {
	
	public Room createRoom(){
        System.out.println("创建房间");
        return new Room();
	}
}

/**2.合成:
 * 房子(超类)
 */
class House{

    private Room room;

    public House(){
    	room=new Room();
    }
    
    //Has-A：合成表示一种强得多的拥有关系，来创造行为
    public void createHouse(){
    	room.createRoom();
    }
}

//船(子类)
class Boat{ 
    public void row(){ 
        System.out.println("开动");
    } 
}

/**3.依赖:
 * 人(超类)
 */
class Person{ 
	
     public void crossRiver(Boat boat){ 
         boat.row(); 
     } 
     //Use-A：表示一种两者无拥有关系的依赖关系
     public void fishing(){ 
         Boat boat =new Boat() ; 
         boat.row(); 
         System.out.println("开始钓鱼！");
     }
} 






