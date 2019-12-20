package com.thomax.letsgo.zoom.network;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadServer {
	
	public static void main(String args[]) throws Exception {
		@SuppressWarnings("resource")
        ServerSocket server = new ServerSocket(8000);

		// 线程池防止并发过高时创建过多线程耗尽资源
		ExecutorService threadPool = Executors.newFixedThreadPool(100);

		while (true) {
			Socket socket = server.accept();
			
			/**接口只有一个需要被实现的方法可以使用这种写法，functional interface*/
			Runnable runnable = () -> {
				try {
					// 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
					InputStream inputStream = socket.getInputStream();
					byte[] bytes = new byte[1024];
					int len;
					StringBuilder sb = new StringBuilder();
					while ((len = inputStream.read(bytes)) != -1) {
						// 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
						sb.append(new String(bytes, 0, len, "UTF-8"));
					}
					System.out.println("get message from client: " + sb);
					inputStream.close();
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
			threadPool.submit(runnable);
		}
	}
	
}
