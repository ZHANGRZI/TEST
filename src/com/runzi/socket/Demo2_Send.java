package com.runzi.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Demo2_Send {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
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
	}

}
