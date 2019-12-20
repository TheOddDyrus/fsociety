package com.thomax.letsgo.zoom.handling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;


public class FileOperations {
	public static void main(String[] args){
		FileWriteRead example = new FileWriteRead();
		example.runFile();
	}
}

/**class1-文件操作*/  
class FileWriteRead {
	String fileName = "Lets Go.txt";
	String secondFile = "secondFile.txt";
	String pathGoJava = System.getProperty("user.dir") + "\\";
	
	public FileWriteRead() {
		//构造备用文件secondFile.txt
		File file = new File(secondFile);
    	if (!file.exists()) {
    		try {
    			file.createNewFile();
    			System.out.println(file.getName() +"创建成功！");
            } catch (IOException e) {
            	e.printStackTrace();
    		}
    	}else {
    		System.out.println(file.getName() + "已经存在！");
    	}
    	
    	try {
            BufferedWriter out = new BufferedWriter(new FileWriter(secondFile));
            out.write("Line1: I" + "\r\n");
            out.write("Line2: Love" + "\r\n");
            out.write("Line3: You");
            out.close(); //关闭
        } catch (IOException e) {
        	e.printStackTrace();
        }
    	System.out.println();
	}

	
    public void runFile()  {
    	//创建文件
    	//java.io.File;
    	File file = new File(fileName);
    	if (!file.exists()) {
    		try {
    			file.createNewFile();
    			System.out.println(file.getName() +"创建成功！");
            } catch (IOException e) {
            	e.printStackTrace();
    		}
    	}else {
    		System.out.println(file.getName() + "已经存在！");
    	}
    	System.out.println();
    		
    	//写入文件
    	//java.io.BufferedWriter; java.io.FileWriter
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter(fileName));
            String str = "Lets Go..." + "\r\n";
            write.write(str);
            write.close(); //关闭
            System.out.println(fileName + "写入内容" + str + "成功！");
        } catch (IOException e) {
        	e.printStackTrace();
        }
        System.out.println();
        
        //将文件内容复制到另一个文件
        //java.io.InputStream; java.io.FileInputStream;
        //java.io.OutputStream; java.io.FileOutputStream;
        try {
        	InputStream input = new FileInputStream(new File(secondFile));
            OutputStream output = new FileOutputStream(new File(fileName), true); //第二个参数代表是否覆盖原有内容,默认为false(覆盖原有内容)
            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) > 0) {
            	output.write(buf, 0, len);
            }
            input.close();
            output.close();
            System.out.println("从" + secondFile + "中拷贝内容写入到" + fileName + "成功！");
        } catch (IOException e) {
        	e.printStackTrace();
        }
        System.out.println();
        
        //读取文件
        //java.io.BufferedReader; java.io.FileReader;
        try {
            BufferedReader read = new BufferedReader(new FileReader(fileName));
            String str;
            System.out.println(fileName + "内容: ");
            while ((str = read.readLine()) != null) {
                System.out.println(str);
            }
            read.close(); //关闭
        } catch (IOException e) {
        	e.printStackTrace();
        }
        System.out.println();
        
        //删除文件
        //java.io.File;
        try{
            if(file.delete()){
                System.out.println(file.getName() + "已被删除！");
            }else{
                System.out.println("删除失败！");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println();
        
        //创建临时文件
        //java.io.File;
        File tempFile = null;
        try {
        	tempFile = File.createTempFile("tmp", ".txt"); // 创建临时文件
            System.out.println("File path: " + tempFile.getAbsolutePath()); // 输出绝对路径
            tempFile.deleteOnExit(); // 终止后删除临时文件
            
            tempFile = File.createTempFile("tmp", ".txt", new File(pathGoJava)); // 创建临时文件
            System.out.println("File path: " + tempFile.getAbsolutePath()); // 输出绝对路径
            tempFile.deleteOnExit(); // 终止后删除临时文件
        } catch(Exception e) {
           e.printStackTrace();
        }
        System.out.println();
        
        //修改文件最后的修改日期
        //java.io.File; java.util.Date
        try {
        	File fileToChange = new File(secondFile);
            Date filetime = new Date(fileToChange.lastModified()); //获取文件最后修改日期(secondFile文件在此类构造函数中会重新创建所以这里的最后修改日期一定是最新的)
            System.out.println(filetime.toString());
            fileToChange.setLastModified(0); //设置文件最后修改日期为1970-1-01
            filetime = new Date(fileToChange.lastModified());
            System.out.println(filetime.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        
        //获取文件大小
        File fileSize = new File(secondFile);
        System.out.println(secondFile + "大小为: " + fileSize.length() + " bytes");
        System.out.println();
        
        //文件重命名
        File oldName = new File(secondFile);
        File newName = new File("thirdFile.txt");
        if(oldName.renameTo(newName)) {  //file.renameTo(newFile)成功以后不会修改file的getname()返回值
            System.out.println(secondFile + "重命名成功！");
        } else {
            System.out.println(secondFile + "重命名失败！");
        }
        newName.renameTo(oldName);
        System.out.println();
        
        //设置文件只读
        File fileRead = new File(secondFile);
        System.out.println("File.setReadOnly() " + fileRead.setReadOnly());
        System.out.println("文件是否可读: " + fileRead.canWrite());
        System.out.println("File.setWritable(true) " + fileRead.setWritable(true));
        System.out.println("文件是否可读: " + fileRead.canWrite());
        System.out.println();
        
        //文件路径比较
        File fileCompare = new File(secondFile);
        File fileCompare2 = new File(pathGoJava + secondFile);
        System.out.println("fileCompare: " + fileCompare.getPath());
        System.out.println("fileCompare2: " + fileCompare2.getPath());
        System.out.println("compareTo(): " + fileCompare.compareTo(fileCompare2)); //File.compareTo() 返回值==0说明文件路径一致
        System.out.println();
        
        //判断是否为隐藏文件
        File fileH = new File(secondFile);
        System.out.println(fileH + "是否为隐藏文件: " + fileH.isHidden());
    }
}
//secondFile.txt已经存在！
//
//Lets Go.txt创建成功！
//
//Lets Go.txt写入内容Lets Go...
//成功！
//
//从secondFile.txt中拷贝内容写入到Lets Go.txt成功！
//
//Lets Go.txt内容: 
//Lets Go...
//Line1: I
//Line2: Love
//Line3: You
//
//Lets Go.txt已被删除！
//
//File path: C:\Users\appeon\AppData\Local\Temp\tmp5672665317201604562.txt
//File path: C:\Users\appeon\Desktop\JavaPG\GoJava\tmp9085085918168021320.txt
//
//Thu Mar 15 09:57:01 CST 2018
//Thu Jan 01 08:00:00 CST 1970
//
//secondFile.txt大小为: 33 bytes
//
//secondFile.txt重命名成功！
//
//File.setReadOnly() true
//文件是否可读: false
//File.setWritable(true) true
//文件是否可读: true
//
//fileCompare: secondFile.txt
//fileCompare2: C:\Users\appeon\Desktop\JavaPG\GoJava\secondFile.txt
//compareTo(): 16
//
//secondFile.txt是否为隐藏文件: false



























