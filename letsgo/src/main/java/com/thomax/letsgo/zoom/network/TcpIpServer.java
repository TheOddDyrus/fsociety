package com.thomax.letsgo.zoom.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpIpServer {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		ServerSocket server = new ServerSocket(8000);
		Socket socket = server.accept();
		InputStream is = socket.getInputStream();
		/*序列化对象，对象需要实现Serializable接口
		ObjectInputStream oos = new ObjectInputStream(is);
		SocketHandling sh = (SocketHandling)oos.readObject();
		*/
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String recieve;
		while ((recieve = br.readLine()) != null) {
			if (recieve.equals("exit")) {
				break;
			}
			System.out.println("服务器接收到的信息：" + recieve);
		}
		System.out.println("\n服务器端开始操作！");
		
		System.err.println("请输入要返回给客户端的信息：");
		OutputStream os = socket.getOutputStream();
		PrintWriter pw = new PrintWriter(os);
		while (sc.hasNext()) {
			String send = sc.nextLine();
			pw.println(send);
			pw.flush();
			if (send.equals("shutdown")) {
				break;
			}
			System.err.println("请输入要返回给客户端的信息：");
		}
		System.out.println("服务器端操作结束！！");

		sc.close();
		br.close();
		pw.close();
		socket.close();
		server.close();
	}

}
