package com.runzi.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Demo2_Server {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
//		demo1();
		final ServerSocket server = new ServerSocket(12345);
		
		while(true) {
			new Thread(){
				@Override
				public void run() {
					try {
						Socket socket = server.accept();
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintStream ps = new PrintStream(socket.getOutputStream());//将字节流包装成字符流
						ps.println("welcome");
						System.out.println(br.readLine());
						ps.println("hi");
						socket.close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}.start();
			
		}
		
	}

	private static void demo1() throws IOException {
		ServerSocket server = new ServerSocket(12345);
		Socket socket = server.accept();
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream ps = new PrintStream(socket.getOutputStream());//将字节流包装成字符流
		ps.println("welcome");
		System.out.println(br.readLine());
		ps.println("hi");
		socket.close();
	}

}
