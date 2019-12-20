package com.thomax.letsgo.zoom.network;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

//import java.io.ObjectOutputStream;

public class TcpIpClient {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		Socket socket = new Socket(InetAddress.getLocalHost(), 8000);
		OutputStream os = socket.getOutputStream();
		/*序列化对象，对象需要实现Serializable接口
		ObjectOutputStream oos = new ObjectOutputStream(os);
		SocketHandling sh = new SocketHandling();
		oos.writeObject(sh);
		*/
		System.err.println("请输入要发送给客户端的信息：");
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(os));
		while (sc.hasNextLine()) {
			String send = sc.nextLine();
			pw.println(send);
			pw.flush();
			if (send.equals("exit")) {
				break;
			}
			System.err.println("请输入要发送给客户端的信息：");
		}
		System.err.println("客户端已结束操作！\n");
		
		InputStream is = socket.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String recieve;
		while ((recieve = br.readLine()) != null) {
			if (recieve.equals("shutdown")) {
				break;
			}
			System.out.println("客户端成功接收消息：" + recieve);
		}
		System.out.println("客户端接收结束！！");
		
		sc.close();
		pw.close();
		br.close();
		socket.close();
	}
	

}
