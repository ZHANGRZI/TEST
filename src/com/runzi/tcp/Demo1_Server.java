package com.runzi.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Demo1_Server {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(12345);
		
		Socket socket = server.accept();
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		
		os.write("nihao".getBytes());
		
		byte[] arr = new byte[2048];
		int len = is.read(arr);
		System.out.println(new String(arr,0,len));
		
		socket.close();
		
	}

}
