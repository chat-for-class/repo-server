package test.Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AcceptThread extends Thread {

	private int port = -1;
	private String threadName = null;
	
	public AcceptThread(int port, String threadName) {
		this.port = port;
		this.threadName = threadName;
	}

	@Override
	public void run() {

		if(threadName != null && !"".equals(threadName)){
			this.setName(threadName);
		}else{
			this.setName("AcceptThread");
		}
		
		ServerSocket serversocket = null;
		try {
		
			System.out.println("Thread Start");
			System.out.println("Thread Name : " + threadName);
			
			if(port != -1){
				serversocket = new ServerSocket(port);
				
				while(true){
					Socket client = serversocket.accept();
					System.out.println("accept from " + client.getLocalAddress().getHostAddress() + ":" + client.getPort());
					new ReadThread(client.getLocalAddress().getHostAddress() + ":" + client.getPort(), client).start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void printConnectionInfo(Socket socket){

		if(socket != null){
			System.out.println("client에서 접속요청 들어옴");
			System.out.println("client정보 : " + socket.getLocalAddress().getHostAddress() + ":" + socket.getPort());
		}
		
	}

}
