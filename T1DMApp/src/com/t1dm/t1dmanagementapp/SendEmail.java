package com.t1dm.t1dmanagementapp;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;




import javax.mail.util.ByteArrayDataSource;

import android.util.Log;


public class SendEmail    {
    String host = "smtp.gmail.com";
         
    String from = "t1dm.team.2013@gmail.com";
    String username = "dheryta";
    String password = "edc@2884RFV12";
    String to="toaddress@gmail.com";
    String subject = "T1DM Report"; 
    Multipart multiPart;
    String content="";
 
    public SendEmail(String to, String content){
    	this.to = to;
    	this.content = content;
    }
    
public void sendEmail(String fileName) throws AddressException, MessagingException{
    Properties properties = System.getProperties();
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.socketFactory.port", "465");
    properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.port", "465");
     
    
    Session session = Session.getDefaultInstance(properties,new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(from,password); 
        }

    });
    
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(from));
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    message.setSubject(subject);
    Multipart multipart = new MimeMultipart();
    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setContent(content, "text/html; charset=ISO-8859-1");
    multipart.addBodyPart(htmlPart);
    
    if (fileName!=null){
    MimeBodyPart imagePart = new MimeBodyPart();
    DataSource dataSource=new FileDataSource(fileName);
    imagePart.setDataHandler(new DataHandler(dataSource));
    imagePart.setFileName("chart.png");
    imagePart.setHeader("Content-Type", "image/png");
    imagePart.setHeader("Content-ID","<image_chart>");
    imagePart.setDisposition(MimeBodyPart.INLINE);
    multipart.addBodyPart(imagePart);
    }
    message.setContent(multipart);     
    
    Transport.send(message);
}
}