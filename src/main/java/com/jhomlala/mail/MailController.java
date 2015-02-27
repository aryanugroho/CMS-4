package com.jhomlala.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailController {

private String from;
private String host;
private Properties properties;
	
public void sendMail(String to,String subject,String message)
{
    Session session = Session.getInstance(properties);
    try {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = {new InternetAddress(to)};
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // Set message content
        msg.setText(message);

        //Send the message
        Transport.send(msg);
    }
    catch (MessagingException mex) {
        // Prints all nested (chained) exceptions as well
        mex.printStackTrace();
    }
}

public MailController()
{
	host = "localhost";
	from = "noreply@gmail.com";
	properties = new Properties();
	properties.put("mail.smtp.host",host);
	properties.put("mail.debug", "true");
}
	 
}
