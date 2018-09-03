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
//����
	public Demo4_GUIchat() {
		init();
		southPanel();
		centerPanel();
		event();
	}

//��ʼ��
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
	
//�м䴰��
	public void centerPanel() {
		Panel center = new Panel();
		viewText = new TextArea();
		sendText = new TextArea(7,1);
		center.setLayout(new BorderLayout()); //���ñ߽粼�ֹ�����
		center.add(sendText,BorderLayout.SOUTH);
		center.add(viewText,BorderLayout.CENTER);
		viewText.setBackground(Color.WHITE);
		viewText.setEditable(false);
		sendText.setFont(new Font("xxx",Font.PLAIN,18));
		viewText.setFont(new Font("xxx",Font.PLAIN,18));
		this.add(center,BorderLayout.CENTER);
	}
		
//�±ߴ���
	public void southPanel() {
		Panel south = new Panel();
		tf = new TextField(15);
		tf.setText("127.0.0.1");
		send = new Button("����");
		log = new Button("��¼");
		clear = new Button("����");
		shake = new Button("��");
		clearAll = new Button("��ռ�¼");
		south.add(tf);
		south.add(send);
		south.add(log);
		south.add(clear);
		south.add(shake);
		south.add(clearAll);
		this.add(south,BorderLayout.SOUTH);
	}
		
//�¼�
	public void event() {
		//�رմ���
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
		
		//���Ͱ�ť
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
		//��¼
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
		
		//����
		clear.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				viewText.setText("");
			}
		});
		
		//��
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
		
		//��ռ�¼
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
	
//��
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
	
//��¼
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
	
//����
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
						bw.write("��");
						continue;
					}
					String message = new String(arr,0,len);
					String ip = packet.getAddress().getHostAddress();
					String time = getCurrentTime();
					String str = time + " " + ip + " ����˵��\r\n" + message + "\r\n\r\n";
					viewText.append(str);
					bw.write(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
//send����
	private void send(byte[] arr,String ip) throws IOException {
		DatagramPacket packet =
				new DatagramPacket(arr, arr.length,InetAddress.getByName(ip),9999);
		socket.send(packet);
	}
//����
	private void send() throws IOException {
		String message = sendText.getText(); //��ȡ������������
		String ip = tf.getText();//��ȡip��ַ
		ip = ip.trim().length() == 0 ? "255.255.255.255" : ip;
		send(message.getBytes(),ip);
		String time = getCurrentTime();
		String str = time + " �Ҷ�" + (ip.equals("255.255.255.255") ? "����" : ip) + "˵\r\n" + message + "\r\n";
		viewText.append(str);
		bw.write(str); //����Ϣд�����ݿ���
		sendText.setText(null);
		
	}
	
//��ȡʱ��
	private String getCurrentTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��HH:mm:ss");
		return sdf.format(d);
	}
	

	//������
	public static void main(String[] args) {
		new Demo4_GUIchat();
	}

}
