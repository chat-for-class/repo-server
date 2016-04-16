package test.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.mysql.jdbc.MySQLConnection;

import test.Connector.MYSQLConnector;
import test.mailSend.SendMail;

public class ReadThread extends Thread {

	private String threadName = null;
	private Socket clientSocket = null;
	private BufferedReader br = null;
	private BufferedWriter bw = null;

	public ReadThread(String threadName, Socket client) {
		this.threadName = threadName;
		this.clientSocket = client;
		
		if(clientSocket != null){
			try {
				br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
				bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		try {
			
			if(threadName != null && !"".equals(threadName)){
				this.setName(threadName);
			}else{
				this.setName("ReadThread");
			}
			
			while(true){

				String message = null;
				
				while((message = br.readLine()) != null){
					if(!message.equals("")){
						String protocolNumber = message.substring(0, message.indexOf("/"));
						if(protocolNumber != null && protocolNumber.length() == 2){
							if(protocolNumber.equals("01") && message.contains("Data:")){
								
								int idStartIndex = message.indexOf("id=") + 3;
								int idEndIndex = message.indexOf(";");
								int pwStartIndex = message.indexOf("password=") + 9;
								int pwEndIndex = message.length();
								
								if(idStartIndex != -1 && idEndIndex != -1 && pwStartIndex != -1 && pwEndIndex != -1){
									String id = message.substring(idStartIndex, idEndIndex);
									String pw = message.substring(pwStartIndex, pwEndIndex);
									
									boolean flag = MYSQLConnector.getInstance().loginCheck(id, pw);
									
									bw.write(String.valueOf(flag)+"\n");
									bw.flush();
								}
							}else if(protocolNumber.equals("02") && message.contains("Data:")){
								
								long serverTime = System.currentTimeMillis();
								
								int mailStartIndex = message.indexOf("mailAddress=") + 12;
								int mailEndIndex = message.length();
								
								String mailAddress = message.substring(mailStartIndex, mailEndIndex);
								
								SendMail sendMail = new SendMail();
								sendMail.init();
								String serverNumber = sendMail.RandomNumber(4);
								sendMail.sendMail(mailAddress, serverNumber);
								
								String accreditationData = null;
								
								while(true){
									accreditationData = br.readLine();
									if(accreditationData.substring(0, 2).equals("03")){
										if(accreditationData.contains("Data:")){
											int timeStartIndex = accreditationData.indexOf("time=") + 5;
											int timeEndIndex = accreditationData.indexOf(";");
											int numberStartIndex = accreditationData.indexOf("number=") + 7;
											int numberEndIndex = accreditationData.length();
											
											if(timeStartIndex != -1 && timeEndIndex != -1 && numberStartIndex != -1 && numberEndIndex != -1){
												long time = Long.valueOf(accreditationData.substring(timeStartIndex, timeEndIndex));
												String number = accreditationData.substring(numberStartIndex ,numberEndIndex);
												if(time - serverTime <= 180000 && serverNumber.equals(number)){
													bw.write("true\n");
													bw.flush();
													break;
												}else{
													bw.write("false\n");
													bw.flush();
												}
												
											}
										}
									}
								}
							}else if(protocolNumber.equals("04") && message.contains("Data:")){
								
								int idStartIndex = message.indexOf("id=") + 3;
								int idEndIndex = message.length();
								
								String id = message.substring(idStartIndex, idEndIndex);
								boolean result = MYSQLConnector.getInstance().CheckIdduplication(id);
								bw.write(result+"\n");
								bw.flush();
								
							}else if(protocolNumber.equals("05") && message.contains("Data:")){
								
//								System.out.println(message.length());
								
								int idStartIndex = message.indexOf("id=") + 3;
								int idEndIndex = message.indexOf(";");
								int pwStartIndex = message.indexOf("password=") + 9;
								int pwEndIndex = message.indexOf(";", (idEndIndex+1));
								int mailStartIndex = message.indexOf("mailAddress=") + 12;
								int mailEndIndex = message.length();
								
								String id = message.substring(idStartIndex, idEndIndex);
								String pw = message.substring(pwStartIndex, pwEndIndex);
								String email = message.substring(mailStartIndex, mailEndIndex);
								
								boolean result = MYSQLConnector.getInstance().insertJoin(id, pw, email);
								bw.write(result+"\n");
								bw.flush();
								
							}else{
								br.reset();
							}
						}
					}
				}
				
				Thread.sleep(100);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
