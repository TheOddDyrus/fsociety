package com.thomax.letsgo.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Date;

public class SocketHandling {
	public static void main(String[] args) throws Exception {
//		GetURLInfo oop = new GetURLInfo();
//		oop.main(args);
	}
}


/**class1-获取指定主机的IP地址*/  //java.net.InetAddress; java.net.UnknownHostException;
class GetIP {
    public static void main(String[] args) {
    	//获取指定主机的IP地址
        InetAddress address = null;
        try {
            address = InetAddress.getByName("www.baidu.com");
        }catch (UnknownHostException e) {
            System.exit(2);
        }
        System.out.println("address -> " + address);
        System.out.println("address.getHostName() -> " + address.getHostName());
        System.out.println("address.getHostAddress() -> " + address.getHostAddress());
        
        //获取本机ip地址及主机名
        InetAddress addrLocal = null;
        try {
        	addrLocal = InetAddress.getLocalHost();
        }catch (UnknownHostException e) {
            System.exit(2);
        } 
        System.out.println("addrLocal -> " + addrLocal);
        System.out.println("addrLocal.getHostAddress() -> " + addrLocal.getHostAddress());
        System.out.println("addrLocal.getHostName() -> " + addrLocal.getHostName());
    }
}
//address -> www.baidu.com/119.75.213.61
//address.getHostName() -> www.baidu.com
//address.getHostAddress() -> 119.75.213.61
//addrLocal -> Fsociety/192.168.1.100
//addrLocal.getHostAddress() -> 192.168.1.100
//addrLocal.getHostName() -> Fsociety


/**class2-查看端口是否已使用*/  //java.net.Socket;
class IfUsePort {
	   public static void main(String[] args) throws IOException {
		  //使用 Socket 连接到指定主机
		  try {
			  InetAddress addr;
			  Socket sock = new Socket("www.baidu.com", 80);
			  addr = sock.getInetAddress();
			  System.out.println("连接到 " + addr);
			  sock.close();
		  } catch (IOException e) {
			  System.out.println("无法连接 " + args[0]);
			  System.out.println(e);
     	  }
		  
		  //查看端口是否已使用
	      Socket Skt = null;
	      String host = "localhost";
	      for (int i = 0; i < 1024; i++) {
	         try {
	            System.out.println("查看 "+ i);
	            Skt = new Socket(host, i);  //Socket(String host, int port)
	            System.out.println("端口 " + i + " 已被使用");
	            
	         }
	         catch (UnknownHostException e) {
	            System.out.println("Exception occured"+ e);
	            break;
	         }
	         catch (IOException e) {
	        	 e.printStackTrace();
	         } finally {
	        	 Skt.close();
	         }
	      }
	      
	   }
}


/**class3-获取远程文件信息*/  //java.net.URL; java.net.URLConnection;
class GetURLInfo {
	   public static void main(String[] args) throws Exception {
		  //获取大小
	      int size;
	      URL url = new URL("http://imgsrc.baidu.com/baike/pic/item/e850352ac65c1038bcba9c0eb9119313b17e8932.jpg");
	      URLConnection conn = url.openConnection();
	      size = conn.getContentLength();
	      if (size < 0)
	          System.out.println("无法获取文件大小。");
	      else
	        System.out.println("文件大小为：" + size + " bytes");
	      conn.getInputStream().close();
	      
	      //获取最后修改日期
	      URL url2 = new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521287572612&di=ff992bc89e68972a5473904ead4f996c&"
	      		             + "imgtype=0&src=http%3A%2F%2Fwww.zuidaima.com%2Fimages%2F19023%2F201411%2F20141108191722328.jpg");
	      URLConnection conn2 = url2.openConnection();
	      //conn.setUseCaches(true);  //如果是本地文件不会报错
	      long timestamp = conn2.getLastModified();
	      Date date = new Date(timestamp);
	      System.out.println("文件最后修改时间 :" + date);
	      conn2.getInputStream().close();
	      
	      //获取URL响应的日期信息  //java.net.HttpURLConnection;
	      URL url4 = new URL("http://www.runoob.com");
	      HttpURLConnection httpCon = (HttpURLConnection) url4.openConnection();
	      long timestamp2 = httpCon.getDate();
	      if (timestamp2 == 0)
	      System.out.println("无法获取信息。");
	      else
	      System.out.println("响应的日期: " + new Date(timestamp2));
	      
	      //解析URL
	      URL url5 = new URL("http://www.runoob.com/html/html-tutorial.html");
	      System.out.println("URL 是 " + url5.toString());
	      System.out.println("协议是 " + url5.getProtocol());
	      System.out.println("验证信息：" + url5.getAuthority());
	      System.out.println("文件名及请求参数 " + url5.getFile());
	      System.out.println("主机是 " + url5.getHost());
	      System.out.println("路径是 " + url5.getPath());
	      System.out.println("端口号是 " + url5.getPort());
	      System.out.println("默认端口号是 " + url5.getDefaultPort());
	      System.out.println("请求参数：" + url5.getQuery());
	      System.out.println("定位位置：" + url5.getRef());
	      System.out.println();
	      
	      //使用 net.URL 类的 URL() 构造函数来抓取网页html内容  //java.io.InputStreamReader;  java.io.BufferedReader; java.io.BufferedWriter; java.io.FileWriter;
	      URL url3 = new URL("http://www.runoob.com/java/net-webpage.html");
	      BufferedReader reader = new BufferedReader(new InputStreamReader(url3.openStream()));
	      BufferedWriter writer = new BufferedWriter(new FileWriter("second.html.txt"));
	      String line;
	      while ((line = reader.readLine()) != null) {
	         System.out.println(line);
	         writer.write(line);
	         writer.newLine();
	      }
	      reader.close();
	      writer.close();
	   }
}
//文件大小为：591637 bytes
//文件最后修改时间 :Sat Nov 08 19:17:22 CST 2014
//响应的日期: Sat Mar 17 17:30:02 CST 2018
//URL 是 http://www.runoob.com/html/html-tutorial.html
//协议是 http
//验证信息：www.runoob.com
//文件名及请求参数 /html/html-tutorial.html
//主机是 www.runoob.com
//路径是 /html/html-tutorial.html
//端口号是 -1
//默认端口号是 80
//请求参数：null
//定位位置：null
//
//<!Doctype html>
//.
//.
//</html>


/**class4-Socket实现多线程服务器程序*/  //java.net.ServerSocket; java.io.PrintStream;
class MultiThreadServer implements Runnable {
	
	   Socket csocket;
	   private static ServerSocket ssock;
	   
	   MultiThreadServer() throws UnknownHostException, IOException {
	      this.csocket = new Socket("localhost", 80);
	   }
	   MultiThreadServer(Socket csocket) {
	      this.csocket = csocket;
	   }
	 
	   public static void main(String args[]) throws Exception {
	      ssock = new ServerSocket(80);
	      System.out.println("Listening");
	      while (true) {
	         Socket sock = ssock.accept();
	         System.out.println("Connected");
	         new Thread(new MultiThreadServer(sock)).start();
	      }
	   }
	   public void run() {
	      try {
	         PrintStream pstream = new PrintStream
	         (csocket.getOutputStream());
	         for (int i = 100; i >= 0; i--) {
	            pstream.println(i + " bottles of beer on the wall");
	         }
	         pstream.close();
	         csocket.close();
	      }
	      catch (IOException e) {
	         System.out.println(e);
	      }
	   }
}


/**class5-ServerSocket和Socket通信实例*/ 
/*建立服务器端:
	服务器建立通信ServerSocket
	服务器建立Socket接收客户端连接
	建立IO输入流读取客户端发送的数据
	建立IO输出流向客户端发送数据消息
*/
class Server {
	
	private static ServerSocket ss;

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
	      try {
	         ss = new ServerSocket(8888);
	         System.out.println("启动服务器....");
	         Socket s = ss.accept();
	         System.out.println("客户端:"+s.getInetAddress().getLocalHost()+"已连接到服务器");
	         
	         BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	         //读取客户端发送来的消息
	         String mess = br.readLine();
	         System.out.println("客户端："+mess);
	         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
	         bw.write(mess+"\n");
	         bw.flush();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }
}

/*建立客户端:
	创建Socket通信，设置通信服务器的IP和Port
	建立IO输出流向服务器发送数据消息
	建立IO输入流读取服务器发送来的数据消息
*/
class Client {
	
	private static Socket s;

	public static void main(String[] args) {
	      try {
	         s = new Socket("127.0.0.1",8888);
	         
	         //构建IO
	         InputStream is = s.getInputStream();
	         OutputStream os = s.getOutputStream();
	         
	         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
	         //向服务器端发送一条消息
	         bw.write("测试客户端和服务器通信，服务器接收到消息返回到客户端\n");
	         bw.flush();
	         
	         //读取服务器返回的消息
	         BufferedReader br = new BufferedReader(new InputStreamReader(is));
	         String mess = br.readLine();
	         System.out.println("服务器："+mess);
	      } catch (UnknownHostException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }
}
























