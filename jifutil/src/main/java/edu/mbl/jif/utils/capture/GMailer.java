/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.utils.capture;


/**
 *
 * @author GBH
 */
public class GMailer {


/*
    public static void main(String[] args) {
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465"); // smtp port
            Authenticator auth = new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("username-gmail", "password-gmail");
                }
            };
            Session session = Session.getDefaultInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("username-gmail@gmail.com"));
            msg.setSubject("Try attachment gmail");
            msg.setRecipient(RecipientType.TO, new InternetAddress("username-gmail@gmail.com"));
            //add atleast simple body
            MimeBodyPart body = new MimeBodyPart();
            body.setText("Try attachment");
            //do attachment
            MimeBodyPart attachMent = new MimeBodyPart();
            FileDataSource dataSource = new FileDataSource(new File("file-sent.txt"));
            attachMent.setDataHandler(new DataHandler(dataSource));
            attachMent.setFileName("file-sent.txt");
            attachMent.setDisposition(MimeBodyPart.ATTACHMENT);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(body);
            multipart.addBodyPart(attachMent);
            msg.setContent(multipart);
            Transport.send(msg);
        } catch (AddressException ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
*/
}