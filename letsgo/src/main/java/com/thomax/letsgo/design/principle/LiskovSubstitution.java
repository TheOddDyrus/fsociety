package com.thomax.letsgo.design.principle;

/**里氏代换原则：只有当衍生类可以替换掉基类，软件单位的功能不受到影响时，基类才能真正被复用，而衍生类也能够在基类的基础上增加新的行为
 *	>任何基类可以出现的地方，子类一定可以出现。 LSP是继承复用的基石，只有当衍生类可以替换掉基类，软件单位的功能不受到影响时，
 * 	  基类才能真正被复用，而衍生类也能够在基类的基础上增加新的行为。
 *	>里氏代换原则是对“开-闭”原则的补充。实现“开-闭”原则的关键步骤就是抽象化。
 *	>而基类与子类的继承关系就是抽象化的具体实现，所以里氏代换原则是对实现抽象化的具体步骤的规范
 */
public class LiskovSubstitution {

	public static void main(String[] args) {
		Rectangle re = new Rectangle(2, 3);
		re.getHeight();
		re.getWidth();
		re.addHeight(2);
		re.addWidth(3);
		Square sq = new Square(2, 3);
		sq.getHeight();
		sq.getWidth();
		sq.addLength(2);
	}

}

//抽象的四边形类,只有取值方法，没有赋值方法
abstract class Quadrangle {
	
	protected long width;
	
	protected long height;
	
	public Quadrangle(long width, long height) {
		this.width = width;
		this.height = height;
	}
	
	public long getWidth() {
		return width;
	}
	
	public long getHeight() {
		return height;
	}
}

//长方形类,赋值方法在实现类中进行
class Rectangle extends Quadrangle {
	
	public Rectangle(long width, long height) {
		super(width, height);
	}
	
	public void addWidth(long width) {
		super.width += width;
	}
	
	public void addHeight(long height) {
		super.height += height;
	}
}

//正方形类,赋值方法在实现类中进行
class Square extends Quadrangle {
	
	public Square(long width, long height) {
		super(width, height);
		if (width != height) {
			System.out.println("对不起，您构造的正方形类不符合常理，用第一个参数" + width +"作为边长");
			super.height = width;
		}
	}
	
	public void addLength(long length) {
		super.width += length;
		super.height += length;
	}
}
