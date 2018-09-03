package com.runzi.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Demo1_Send {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String str = "what are you doing";
		DatagramSocket socket = new DatagramSocket();//创建socket,相当于创建码头
		DatagramPacket packet = 						//创建packet,相当于集装箱
				new DatagramPacket(str.getBytes(), str.getBytes().length,InetAddress.getByName("127.0.0.1"),6666);
		socket.send(packet);			//将数据发出去，发货
		socket.close();      //关闭码头，底层是io流
	}

}
