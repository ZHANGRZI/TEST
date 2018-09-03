package com.runzi.socket;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Demo4_GUIchat extends Frame {
	private TextField tf;
	private Button send;
	private Button log;
	private Button clear;
	private Button shake;
	private Button clearAll;
	private TextArea viewText;
	private TextArea sendText;
	private DatagramSocket socket;
	private BufferedWriter bw;
	/**
	 * @param args
	 */
//构造
	public Demo4_GUIchat() {
		init();
		southPanel();
		centerPanel();
		event();
	}

//初始化
	public void init() {
		this.setLocation(500, 50);
		this.setSize(400,800);
		new Receive().start();
		try {
			socket = new DatagramSocket();
			bw = new BufferedWriter(new FileWriter("config.txt",true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}
	
//中间窗口
	public void centerPanel() {
		Panel center = new Panel();
		viewText = new TextArea();
		sendText = new TextArea(7,1);
		center.setLayout(new BorderLayout()); //设置边界布局管理器
		center.add(sendText,BorderLayout.SOUTH);
		center.add(viewText,BorderLayout.CENTER);
		viewText.setBackground(Color.WHITE);
		viewText.setEditable(false);
		sendText.setFont(new Font("xxx",Font.PLAIN,18));
		viewText.setFont(new Font("xxx",Font.PLAIN,18));
		this.add(center,BorderLayout.CENTER);
	}
		
//下边窗口
	public void southPanel() {
		Panel south = new Panel();
		tf = new TextField(15);
		tf.setText("127.0.0.1");
		send = new Button("发送");
		log = new Button("记录");
		clear = new Button("清屏");
		shake = new Button("震动");
		clearAll = new Button("清空记录");
		south.add(tf);
		south.add(send);
		south.add(log);
		south.add(clear);
		south.add(shake);
		south.add(clearAll);
		this.add(south,BorderLayout.SOUTH);
	}
		
//事件
	public void event() {
		//关闭窗口
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					socket.close();
					bw.close();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		//发送按钮
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					send();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		//记录
		log.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					logFile();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			}

			
		});
		
		//清屏
		clear.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				viewText.setText("");
			}
		});
		
		//震动
		shake.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					send(new byte[]{-1},tf.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});
		
		//清空记录
		clearAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new FileWriter("config.txt");
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			}
		});
		
		sendText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER /*&& e.isControlDown()*/){
					try {
						send();
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
//震动
	private void shake() {
		int x = this.getLocation().x;
		int y = this.getLocation().y;
		
		for(int i = 0; i < 5; i++){
			try {
				this.setLocation(x+20,y+20);
				Thread.sleep(20);
				this.setLocation(x+20,y-20);
				Thread.sleep(20);
				this.setLocation(x-20,y+20);
				Thread.sleep(20);
				this.setLocation(x-20,y-20);
				Thread.sleep(20);
				this.setLocation(x-20,y-30);
				Thread.sleep(20);
				this.setLocation(x-30,y-30);
				Thread.sleep(20);
				this.setLocation(x,y);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}
	
//记录
	private void logFile() throws IOException {
		bw.flush();
		FileInputStream fis = new FileInputStream("config.txt");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		int len;
		byte[] arr = new byte[8192];
		while((len = fis.read(arr)) != -1) {
			baos.write(arr,0,len);
		}
		String str = baos.toString();
		viewText.setText(str);
		
		fis.close();
	}
	
//接收
	private class Receive extends Thread {
		public void run() {
			try {
				DatagramSocket socket = new DatagramSocket(9999);
				DatagramPacket packet = new DatagramPacket(new byte[8192], 8192);
				
				while(true){
					socket.receive(packet);
					byte[] arr = packet.getData();
					int len = packet.getLength();
					if(arr[0] == -1 && len == 1){
						shake();
						bw.write("震动");
						continue;
					}
					String message = new String(arr,0,len);
					String ip = packet.getAddress().getHostAddress();
					String time = getCurrentTime();
					String str = time + " " + ip + " 对我说：\r\n" + message + "\r\n\r\n";
					viewText.append(str);
					bw.write(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
//send重载
	private void send(byte[] arr,String ip) throws IOException {
		DatagramPacket packet =
				new DatagramPacket(arr, arr.length,InetAddress.getByName(ip),9999);
		socket.send(packet);
	}
//发送
	private void send() throws IOException {
		String message = sendText.getText(); //获取发送区域内容
		String ip = tf.getText();//获取ip地址
		ip = ip.trim().length() == 0 ? "255.255.255.255" : ip;
		send(message.getBytes(),ip);
		String time = getCurrentTime();
		String str = time + " 我对" + (ip.equals("255.255.255.255") ? "宝宝" : ip) + "说\r\n" + message + "\r\n";
		viewText.append(str);
		bw.write(str); //将信息写到数据库中
		sendText.setText(null);
		
	}
	
//获取时间
	private String getCurrentTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
		return sdf.format(d);
	}
	

	//主方法
	public static void main(String[] args) {
		new Demo4_GUIchat();
	}

}
