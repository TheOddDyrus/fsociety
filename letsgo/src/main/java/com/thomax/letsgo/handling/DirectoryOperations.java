package com.thomax.letsgo.handling;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class DirectoryOperations {
	public static void main(String[] args){
		Directory example = new Directory();
		example.runDirectory();
	}
}

/**class1-目录操作*/  
class Directory {
	String pathGoJava;
	
	public Directory() {
		FileWriteRead fwr = new FileWriteRead();
		this.pathGoJava = fwr.pathGoJava;  //已经优化在右侧加上了"\\"
		System.out.println("构造函数初始化pathGoJava: " + this.pathGoJava);
		System.out.println();
	}
	
	public void runDirectory() {
		//递归创建目录
		String directories = pathGoJava + "a\\b\\c\\d\\e\\f\\g\\h\\i";
        File file = new File(directories);
    	if(!file.exists()) {
        	System.out.println("递归目录a-i是否成功创建: " + file.mkdirs());  //File.mkdirs()
        }else {
        	System.out.println("目录已经存在... " + directories);
        }
    	//创建备用文件****
    	try { 
    		File fileBak1 = new File(pathGoJava + "a\\b\\c\\" + "New Text Document1.txt");
    		File fileBak2 = new File(pathGoJava + "a\\b\\c\\d\\e\\f\\g\\" + "New Text Document2.txt");
    		File fileBak3 = new File(pathGoJava + "a\\b\\c\\d\\e\\f\\g\\" + "New Text Document3.txt");
    		File fileBak4 = new File(pathGoJava + "a\\b\\c\\d\\e\\f\\g\\h\\" + "New Text Document4.txt");
    		boolean fileBak = true;
    		if(!fileBak1.createNewFile()) {
    			System.out.println(fileBak1 + "备用文件创建失败");
    			fileBak = false;
    		}  
    		if(!fileBak2.createNewFile()) {
    			System.out.println(fileBak2 + "备用文件创建失败");
    			fileBak = false;
    		}  
    		if(!fileBak3.createNewFile()) {
    			System.out.println(fileBak3 + "备用文件创建失败");
    			fileBak = false;
    		} 
    		if(!fileBak4.createNewFile()) {
    			System.out.println(fileBak4 + "备用文件1创建失败");
    			fileBak = false;
    		} 
    		if(fileBak)  System.out.println("四个备用文件创建成功！");
    	}catch(IOException e){
    		e.printStackTrace();
    	} 
    	//创建备用文件****
        System.out.println();
        
        //获取目录大小
        //import org.apache.commons.io.FileUtils;
        //long size = FileUtils.sizeOfDirectory(new File(".\\a\\b\\c"));
        //System.out.println("Size: " + size + " bytes");
        //System.out.println();
        
        //打印目录结构
        System.out.println("打印目录结构:");
        showDir(1, new File(".\\a\\b\\c"));
        System.out.println();
        

        //获取上级目录
        File parentDirectory = new File(".\\a\\b\\c");
        System.out.println(parentDirectory + "的上级目录为 : " + parentDirectory.getParent());
        System.out.println();
        
        //获取目录的最后修改时间
        File fileModify = new File("./a/b");
        System.out.println(fileModify + "最后修改时间：" + new Date(fileModify.lastModified()));
        System.out.println();
        
        //遍历系统根目录
        File[] roots = File.listRoots(); //返回File[]
        System.out.println("系统所有根目录：");
        for (int i=0; i < roots.length; i++) {
            System.out.println(roots[i].toString());
        }
        System.out.println();
        
        //查看当前工作目录
        System.out.println("当前的工作目录为: " + System.getProperty("user.dir"));
        System.out.println();
        
      //删除目录
        boolean boolDelete = deleteDir(new File("./a"));  // ("./a") == (".\\a") == (pathGoJava + "\\a")
        System.out.println("递归删除目录结果: " + boolDelete);
        System.out.println();
	}
	
	//递归删除目录内所有内容
	public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {  //判断目录否存在
        	if(dir.list().length>0){
                System.out.println(dir + "不为空!");
            }else{
                System.out.println(dir + "为空!");
            }
        	
            String[] children = dir.list();  //返回String[]
            for (int i = 0; i < children.length; i++) {
                if(!deleteDir(new File(dir, children[i]))) {   //File(目录，目录内的【目录/文件】名)
                	return false; 
                }
            }
        }
        if(dir.delete()) {  //删除目录或文件
            System.out.println(dir + "已被删除！");
            return true;
        } else {
            System.out.println(dir + "删除失败！");
            return false;
        }
    }
	
	//打印目录内结构
    public void showDir(int indent, File file) {
        for (int i = 0; i < indent; i++)
            System.out.print('-');
        System.out.println(file.getName());
        if (file.isDirectory()) {
            File[] files = file.listFiles();  //返回File[]
            for (int i = 0; i < files.length; i++)
                showDir(indent + 4, files[i]);
        }
    }
      
}
//secondFile.txt已经存在！
//
//构造函数初始化pathGoJava: C:\Users\appeon\Desktop\JavaPG\GoJava\
//
//递归目录a-i是否成功创建: true
//四个备用文件创建成功！
//
//打印目录结构:
//-c
//-----d
//---------e
//-------------f
//-----------------g
//---------------------h
//-------------------------i
//-------------------------New Text Document4.txt
//---------------------New Text Document2.txt
//---------------------New Text Document3.txt
//-----New Text Document1.txt
//
//.\a\b\c的上级目录为 : .\a\b
//
//.\a\b最后修改时间：Thu Mar 15 13:56:49 CST 2018
//
//系统所有根目录：
//C:\
//D:\
//
//当前的工作目录为: C:\Users\appeon\Desktop\JavaPG\GoJava
//
//.\a不为空!
//.\a\b不为空!
//.\a\b\c不为空!
//.\a\b\c\d不为空!
//.\a\b\c\d\e不为空!
//.\a\b\c\d\e\f不为空!
//.\a\b\c\d\e\f\g不为空!
//.\a\b\c\d\e\f\g\h不为空!
//.\a\b\c\d\e\f\g\h\i为空!
//.\a\b\c\d\e\f\g\h\i已被删除！
//.\a\b\c\d\e\f\g\h\New Text Document4.txt已被删除！
//.\a\b\c\d\e\f\g\h已被删除！
//.\a\b\c\d\e\f\g\New Text Document2.txt已被删除！
//.\a\b\c\d\e\f\g\New Text Document3.txt已被删除！
//.\a\b\c\d\e\f\g已被删除！
//.\a\b\c\d\e\f已被删除！
//.\a\b\c\d\e已被删除！
//.\a\b\c\d已被删除！
//.\a\b\c\New Text Document1.txt已被删除！
//.\a\b\c已被删除！
//.\a\b已被删除！
//.\a已被删除！
//递归删除目录结果: true








