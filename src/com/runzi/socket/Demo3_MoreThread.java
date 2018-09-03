package com.runzi.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Demo3_MoreThread {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		new Receive().start();
		Thread.sleep(100);
		new Send().start();
	}

}
class Receive extends Thread {
	public void run() {
		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Send extends Thread{
	public void run() {
		try {
			Scanner sc = new Scanner(System.in);
			DatagramSocket socket = new DatagramSocket();//创建socket,相当于创建码头
			
			while(true) {
				String line = sc.nextLine();
				if ("quit".equals(line)) {
					break;
				}
				DatagramPacket packet = 						//创建packet,相当于集装箱
						new DatagramPacket(line.getBytes(), line.getBytes().length,InetAddress.getByName("127.0.0.1"),6666);
				socket.send(packet);			//将数据发出去，发货
			}
			socket.close();      //关闭码头，底层是io流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}