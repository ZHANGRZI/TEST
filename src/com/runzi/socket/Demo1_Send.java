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
		DatagramSocket socket = new DatagramSocket();//����socket,�൱�ڴ�����ͷ
		DatagramPacket packet = 						//����packet,�൱�ڼ�װ��
				new DatagramPacket(str.getBytes(), str.getBytes().length,InetAddress.getByName("127.0.0.1"),6666);
		socket.send(packet);			//�����ݷ���ȥ������
		socket.close();      //�ر���ͷ���ײ���io��
	}

}
