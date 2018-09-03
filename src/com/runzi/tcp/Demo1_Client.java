package com.runzi.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Demo1_Client {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1",12345);
		
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		
		byte[] arr = new byte[2048];
		int len = is.read(arr);
		System.out.println(new String(arr,0,len));
		
		os.write("ÄãºÃ".getBytes()); 
		socket.close();
	}

}
