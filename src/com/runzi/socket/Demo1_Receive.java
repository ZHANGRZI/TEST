package com.runzi.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Demo1_Receive {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(6666);  //����socket�൱�ڴ�����ͷ
		DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);  //����packet�൱�ڴ�����װ��
		socket.receive(packet);    //�������ݣ��൱�ڽӻ�
		
		byte[] arr = packet.getData();//��ȡ����
		int len = packet.getLength();//��ȡ��Ч���ֽڸ���
		System.out.println(new String(arr,0,len));
		socket.close();
	}

}
