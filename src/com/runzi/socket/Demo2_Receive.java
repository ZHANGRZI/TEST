package com.runzi.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Demo2_Receive {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(6666);  //创建socket相当于创建码头
		DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);  //创建packet相当于创建集装箱
		
		while(true) {
			socket.receive(packet);    //接受数据，相当于接货
			
			byte[] arr = packet.getData();//获取数据
			int len = packet.getLength();//获取有效的字节个
			String ip = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			System.out.println(ip + ":" + port + ":" + new String(arr,0,len));
		}
		
	}

}
