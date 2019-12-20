package com.thomax.letsgo.zoom.handling;


public class SystemGetProperty {
/**System.getProperties()取值：
	java.version  Java运行时环境版本 
	java.vendor  Java运行时环境供应商 
	java.vendor.url  Java供应商的 URL 
	java.home  Java安装目录 
	java.vm.specification.version  Java虚拟机规范版本 
	java.vm.specification.vendor  Java虚拟机规范供应商 
	java.vm.specification.name  Java虚拟机规范名称 
	java.vm.version  Java虚拟机实现版本 
	java.vm.vendor  Java虚拟机实现供应商 
	java.vm.name  Java虚拟机实现名称 
	java.specification.version  Java运行时环境规范版本 
	java.specification.vendor  Java运行时环境规范供应商 
	java.specification.name  Java运行时环境规范名称 
	java.class.version  Java类格式版本号 
	java.class.path  Java类路径 
	java.library.path  加载库时搜索的路径列表 
	java.io.tmpdir  默认的临时文件路径 
	java.compiler  要使用的JIT编译器的名称 
	java.ext.dirs  一个或多个扩展目录的路径 
	os.name  操作系统的名称 
	os.arch  操作系统的架构 
	os.version  操作系统的版本 
	file.separator  文件分隔符（在 UNIX 系统中是“/”） 
	path.separator  路径分隔符（在 UNIX 系统中是“:”） 
	line.separator  行分隔符（在 UNIX 系统中是“/n”） 
	user.name  用户的账户名称 
	user.home  用户的主目录 
	user.dir  用户的当前工作目录
*/
	public void runExample() {
        String str = System.getProperty("user.dir");
	}
}
