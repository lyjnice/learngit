
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

public class sendEmail {
    public static void main(String[] args) throws MessagingException {
    	// 1.�������� ���ӵ�������
		Properties props = new Properties();
		Session session = Session.getInstance(props, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("1582508438@qq.com","11111111111");
			}
		});
		
		//�����ʼ�����
		
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("1582508438@qq.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress());
		message.setSubject("�������ǵļ����ʼ�");
		message.setContent("�������ݡ�������������������������������������","");
		
		//�����ʼ�
		Transport.send(message);
	}
}
