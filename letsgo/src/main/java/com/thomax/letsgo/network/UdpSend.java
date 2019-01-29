package com.thomax.letsgo.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UdpSend {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		DatagramSocket socket = new DatagramSocket();
		System.err.println("请输入发送的信息：");
		while (sc.hasNextLine()) {
			String send = sc.nextLine();
			byte[] b = send.getBytes();
			DatagramPacket packet = new DatagramPacket(b, b.length,
					                               InetAddress.getLocalHost(), 8001);
			socket.send(packet);
			if (send.equals("exit")) {
				break;
			}
			System.err.println("请输入发送的信息：");
		}
		System.out.println("发送信息结束！！");
		
		sc.close();
		socket.close();
	}

}
