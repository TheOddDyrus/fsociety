package com.thomax.letsgo.zoom.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpReceive {

	public static void main(String[] args) throws Exception {
		DatagramSocket socket = new DatagramSocket(8001);
		String receive;
		do {
			byte[] buf = new byte[100];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			receive = new String(packet.getData(), 0, packet.getLength());
			System.err.println("接收到的信息是：");
			System.out.println(receive);
		} while (!receive.equals("exit"));
		System.out.println("接收信息结束！！");
		
		socket.close();
	}

}
