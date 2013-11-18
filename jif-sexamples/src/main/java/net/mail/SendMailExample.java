/*
 From http://www.techlabs4u.com/2010/08/java-code-to-send-email-using-java-mail.html
 */
package net.mail;

import java.io.File;
import java.io.FileFilter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;


/*
 * Send using GMail account: user:sternidae001 pwd:sternidae
 * 
 */


public class SendMailExample {

	public static void main(String[] args) {
		String host = getHostName();
		System.out.println("host = " + host);
		String from = "sender@gmail.com";
		String to = "gharris@mbl.edu";
		String subject = "Test Message";
		String message = "This is A test message sent via Gmail ";
		// create temp directory
		// 
		String[] attachments = {"C:/sz_desktop/gbh ongoing/GBH Bio/GBHarris Resume 2011.rtf"};
		// Build Attachments
		// Java System Properties listing
		// CoreLog fileset
//		String path = System.getProperty("user.dir") + File.separator + "*.*";  //"CoreLog*.txt";
//		System.out.println("path = " + path);
//		for(File f : new FileSet(path))
//                 System.out.println(f);

		SendViaGmail sendMail = new SendViaGmail(from, to, subject, message, attachments);
		sendMail.send();
	}

	static String getHostName() {
		String hostname = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			byte[] ipAddr = addr.getAddress();
			hostname = addr.getHostName();
		} catch (UnknownHostException e) {
		}
		return hostname;
	}

}

class SendViaGmail {

	private String from;
	private String to;
	private String subject;
	private String text;
	private final String[] attachments;

	public SendViaGmail(String from, String to, String subject, String text, String[] attachments) {
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.text = text;
		this.attachments = attachments;
	}

	public void send() {
		String host = "smtp.gmail.com";
		String userid = "sternidae001";
		String password = "sternidae";
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.setProperty("mail.transport.protocol", "smtps");
			props.put("mail.smtp.user", userid);
			props.put("mail.smtp.password", password);
			props.put("mail.smtp.port", "465");
			props.put("mail.smtps.auth", "true");

			// Addresses
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
			try {
				fromAddress = new InternetAddress(from);
				toAddress = new InternetAddress(to);
			} catch (AddressException e) {
				e.printStackTrace();
			}

			Session session = Session.getDefaultInstance(props, null);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(fromAddress);
			message.setRecipient(RecipientType.TO, toAddress);
			message.setSubject(subject);
			// Multipart
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(text);

			// Add message text
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Attachments should reside in your server.
			// Example "c:\file.txt" or "/home/user/photo.jpg"
			if (attachments != null) {
				for (int i = 0; i < attachments.length; i++) {
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(attachments[i]);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(attachments[i]);
					multipart.addBodyPart(messageBodyPart);
				}
			}
			message.setContent(multipart);

			//SMTPSSLTransport transport =(SMTPSSLTransport)session.getTransport("smtps");

			Transport transport = session.getTransport("smtps");
			transport.connect(host, userid, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}

/**
 * This class is part of the <A HREF=http://www.mpi-inf.mpg.de/~suchanek/downloads/javatools target=_blank> Java Tools
 * <A HREF=mailto:f.m.suchanek@web.de>Fabian M. Suchanek</A><P>
 *
 * The class represents a set of files as given by a wildcard string. It does not include folders and is not
 * case-sensitive.<BR> Example:
 * <PRE>
 * for(File f : new FileSet("c:\\myfiles\\*.jAvA"))
 * System.out.println(f);
 * -->
 * c:\myfiles\FileSet.java
 * c:\myfiles\HTMLReader.java
 * ...
 * </PRE>
 */
class FileSet extends ArrayList<File> {

	/**
	 * Constructs a file from a folder, subfolder names and a filename
	 */
	public static File file(File f, String... s) {
		for (String n : s) {
			f = new File(f, n);
		}
		return (f);
	}

	/**
	 * Constructs a FileSet from a wildcard string (including path)
	 */
	public FileSet(String s) {
		this(new File(s));
	}

	/**
	 * Constructs a FileSet from a wildcard string (including path)
	 */
	public FileSet(File f) {
		this((f.getParentFile() == null) ? new File(".") : f.getParentFile(), f.getName());
	}

	/**
	 * Constructs a FileSet from a wildcard string
	 */
	public FileSet(File path, String fname) {
		String regex = "";
		for (int i = 0; i < fname.length(); i++) {
			switch (fname.charAt(i)) {
				case '.':
					regex += "\\.";
					break;

				case '?':
					regex += '.';
					break;

				case '*':
					regex += ".*";
					break;

				default:
					regex += ("" + '[' + Character.toLowerCase(fname.charAt(i))
							+ Character.toUpperCase(fname.charAt(i)) + ']');
			}
		}
		final Pattern wildcard = Pattern.compile(regex);
		File[] files = path.listFiles(
				new FileFilter() {
					public boolean accept(File pathname) {
						return (wildcard.matcher(pathname.getName()).matches());
					}
				});

		// Stupid, but the internal array is inaccessible
		if (files == null) {
			throw new RuntimeException("Can't find files in " + path);
		}
		ensureCapacity(files.length);
		for (File f1 : files) {
			add(f1);
		}
	}

	/**
	 * Exchanges the extension of a filename
	 */
	public static File newExtension(File f, String newex) {
		return (new File(newExtension(f.getPath(), newex)));
	}

	/**
	 * Exchanges the extension of a filename
	 */
	public static String newExtension(String f, String newex) {
		// Extension may be given with preceding dot or without
		if (newex.startsWith(".")) {
			newex = newex.substring(1);
		}
		int i = f.lastIndexOf('.');

		// If the task is to delete the extension...
		if (newex.length() == 0) {
			if (i == -1) {
				return (f);
			}
			return (f.substring(0, i));
		}

		// Else add or replace the extension
		if (i == -1) {
			return (f + '.' + newex);
		}
		return (f.substring(0, i) + '.' + newex);
	}

}
