/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.meteocal.security;

import java.util.Date;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Filippo
 */
@Stateless
public class EmailSessionBean {

    public enum Protocol {

        SMTP,
        SMTPS,
        TLS
    }

    private int port = 587;
    private String host = "smtp.gmail.com";
    private String from = "infometeocal@gmail.com";
    private boolean auth = true;
    private String username = "infometeocal@gmail.com";
    private String password = "Meteocal1";
    private Protocol protocol = Protocol.TLS;
    private boolean debug = false;

    public void sendEmail(String to, String subject,String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        switch (protocol) {
            case SMTPS:
                props.put("mail.smtp.ssl.enable", true);
                break;
            case TLS:
                props.put("mail.smtp.starttls.enable", true);
                break;
        }
        Authenticator authenticator = null;
        if (auth) {
            props.put("mail.smtp.auth", true);
            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(username, password);

                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }

        Session session = Session.getInstance(props, authenticator);
        session.setDebug(debug);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }

    }
    
    
    public void sendInviteEmail(String to, String eventTitle, String inviter){
        String body="You've been invited to the event "+eventTitle+" by "+inviter+" visit www.meteocal.net:8080 for more information";
        String subject="Invite for "+eventTitle;
        sendEmail(to, subject, body);
    }
    
    public void sendResponseEmail(String to, String eventTitle, String sender, boolean answer){
        String body="You received a response for your invite to "+eventTitle+" sent by "+sender+": his answer is "+ (answer?"Yes":"No") +" visit www.meteocal.net:8080 for more information";
        String subject="Response from "+sender;
        sendEmail(to, subject, body);
    }
    
    public void sendWeatherEmail(String to,String eventTitle,String weatherConditions){
        String body="The weather condition for your event "+eventTitle+" are forecasted to be "+weatherConditions+" visit www.meteocal.net:8080 for more information";
        String subject="Weather Forecast for "+eventTitle;
        sendEmail(to, subject, body);
    }
    
    public void sendChangedEvent(String to,String eventTitle){
        String body="Some changes have been made to the event: "+eventTitle+", visit www.meteocal.net:8080 for more information";
        String subject="Changes in "+eventTitle;
        sendEmail(to, subject, body);
    }
    
    public void sendAdminEmail(String to, String eventTitle){
        String body="Some changes have been made to the type of the event: "+eventTitle+" by the admin, visit www.meteocal.net:8080 for more information";
        String subject="Changes in "+eventTitle;
        sendEmail(to, subject, body);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
