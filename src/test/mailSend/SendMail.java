package test.mailSend;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	private Properties props = null;
	
	public void init(){
		props = System.getProperties();
		props.setProperty("mail.smtp.host", "127.0.0.1");
	}
	
	public boolean sendMail(String mailAddress, String number){
		
		boolean flag = false;

		if(props != null){
		
			Session session = Session.getDefaultInstance(props);
			MimeMessage msg = new MimeMessage(session);
		
			try {
				msg.setFrom(new InternetAddress("test@test.com", "Test"));
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress, "Kain Kim"));
				msg.setSubject("인증번호 안내 메일");
				msg.setContent("인증번호 안내 메일입니다\n인증번호 : "+number, "text/html; charset=utf-8");
				
				Transport.send(msg);
				flag = true;
				
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return flag;
	}

	public String RandomNumber(int cipher){
		String result = "";
		int[] data = {1,2,3,4,5,6,7,8,9};
		
		while(true){
			int index = ((int)(Math.random() * (data.length-1)));
			
			if(data[index] != -1){
				result += data[index];
				data[index] = -1;
			}
			
			if(result.length() == cipher){
				break;
			}
		}
		
		return result;
	}
	
}
