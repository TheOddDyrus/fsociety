package com.thomax.letsgo.design.pattern.structure;

import java.util.Enumeration;
import java.util.Vector;

/**组合模式：
 *	>组合模式有时又叫部分-整体模式在处理类似树形结构的问题时比较方便
 *	>使用场景：将多个对象组合在一起进行操作，常用于表示树形结构中，例如二叉树
 */
public class Composite {
	public static void main(String[] args) {
		TreeNode tree = new TreeNode("A");  //设置树干
		tree.printTree();
		System.out.println();
		
		TreeNode treeNode1 = new TreeNode("BB");  //设置枝干
		TreeNode treeNode2 = new TreeNode("CC");  //设置枝干
		TreeNode treeNode3 = new TreeNode("DDD");  //设置枝干
		TreeNode treeNode4 = new TreeNode("EEE");  //设置枝干
		TreeNode treeNode5 = new TreeNode("FFF");  //设置枝干
		
		treeNode1.add(treeNode3);
		treeNode1.printTree();
		System.out.println();
		
		treeNode1.add(treeNode4);
		treeNode1.printTree();
		System.out.println();
		
		treeNode2.add(treeNode5);
		treeNode2.printTree();
		System.out.println();
		
		tree.add(treeNode1);
		tree.printTree();
		System.out.println();
		
		tree.add(treeNode2);
		tree.printTree();
		System.out.println();
		
		tree.remove(treeNode1);
		tree.printTree();
	}
}

//枝干
class TreeNode {  
    private String name;
    private Vector<TreeNode> children = new Vector<TreeNode>();  //枝干的节点有多个
    private int grade = 0;
    
	public TreeNode(String name){
        this.name = name;  
    }  
	
    public String getName() {
        return name;  
    }  
    public void setName(String name) {
        this.name = name;  
    }  
    
    public int getGrade() {
		return grade;
	}
    //设置枝干的等级
	public void setGrade(int grade) {
		this.grade = grade;
	}
   
    //递归-枝干等级+1
	public void addGrade() {
		this.grade++;
		Enumeration<TreeNode> en = getChildren();
		while (en.hasMoreElements()) {
    		TreeNode tn = en.nextElement();
    		tn.addGrade();
    	}
	}
    
    //添加新枝干到此枝干的节点
    public void add(TreeNode node){  
    	node.setGrade(grade); //将节点上的等级设置与自己一样
        node.addGrade(); //节点内的枝干与节点递归遍历等级+1
        children.add(node); 
    }  
      
    //指定删除此枝干的某个节点,节点的子节点删除不了
    public void remove(TreeNode node){  
        children.remove(node);  
    }  
      
    //取得此枝干内所有节点
    public Enumeration<TreeNode> getChildren(){
        return children.elements();  
    }  
    
    //打印枝干与节点的树状图，树干等级=0，树枝越长等级越高
    public void printTree() {
    	for (int i = 0; i < grade; i++) {
    		System.out.print("  ");
    	}
    	System.out.println("|---" + name);
    	Enumeration<TreeNode> en = getChildren();
    	while (en.hasMoreElements()) {
    		TreeNode tn = en.nextElement();
    		tn.printTree();
    	}
    }
}  










