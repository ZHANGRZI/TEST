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
			DatagramSocket socket = new DatagramSocket(6666);  //����socket�൱�ڴ�����ͷ
			DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);  //����packet�൱�ڴ�����װ��
			
			while(true) {
				socket.receive(packet);    //�������ݣ��൱�ڽӻ�
				
				byte[] arr = packet.getData();//��ȡ����
				int len = packet.getLength();//��ȡ��Ч���ֽڸ�
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
			DatagramSocket socket = new DatagramSocket();//����socket,�൱�ڴ�����ͷ
			
			while(true) {
				String line = sc.nextLine();
				if ("quit".equals(line)) {
					break;
				}
				DatagramPacket packet = 						//����packet,�൱�ڼ�װ��
						new DatagramPacket(line.getBytes(), line.getBytes().length,InetAddress.getByName("127.0.0.1"),6666);
				socket.send(packet);			//�����ݷ���ȥ������
			}
			socket.close();      //�ر���ͷ���ײ���io��
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}