package edu.mbl.jif.utils.capture;

/**
 *
 * @author GBH
 */
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

// from: http://kahimyang.info/kauswagan/howto_blogs/536-a_java_class_for_sending_multipart_email_messages_through_your_gmail_account
public class Email {

	public void send(String recipeintEmail,
			String subject,
			String messageText,
			String[] attachments)
			throws MessagingException, AddressException {
		/*
		 * It is a good practice to put this in a java.util.Properties file and encrypt password. Scroll down to comments
		 * below to see how to use java.util.Properties in JSF context.
		 */
		String senderEmail = "tnargsirrah@gmail.com";
		String senderMailPassword = "hihohiH0";
		String gmail = "smtp.gmail.com";

		Properties props = System.getProperties();

		props.put("mail.smtp.user", senderEmail);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.debug", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		// Required to avoid security exception.
		MyAuthenticator authentication = new MyAuthenticator(senderEmail, senderMailPassword);
		Session session = Session.getDefaultInstance(props, authentication);
		session.setDebug(true);
		MimeMessage message = new MimeMessage(session);
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(messageText);
		// Add message text
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// Attachments should reside in your server.
		// Example "c:\file.txt" or "/home/user/photo.jpg"
		for (int i = 0; i < attachments.length; i++) {
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(attachments[i]);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(attachments[i]);
			multipart.addBodyPart(messageBodyPart);
		}
		message.setContent(multipart);
		message.setSubject(subject);
		message.setFrom(new InternetAddress(senderEmail));
		message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipeintEmail));

		Transport transport = session.getTransport("smtps");
		transport.connect(gmail, 465, senderEmail, senderMailPassword);
		System.out.println("Connected to transport.");
		transport.sendMessage(message, message.getAllRecipients());

		transport.close();

	}

	private class MyAuthenticator extends javax.mail.Authenticator {

		String User;
		String Password;

		public MyAuthenticator(String user, String password) {
			User = user;
			Password = password;
		}

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new javax.mail.PasswordAuthentication(User, Password);
		}

	}

	public static void main(String[] args) {
		try {
			new Email().send("gharris@mbl.edu", "Subject", "This is the Message", new String[]{});
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
	}

}