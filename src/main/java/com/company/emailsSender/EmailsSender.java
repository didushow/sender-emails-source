package com.company.emailsSender;

import pedido.Pedido;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailsSender {

    private static final String HOST_SMTP = "mail.smtp.host";
    private static final String HOST_SMTP_GMAIL = "smtp.gmail.com";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final String SOCKET_FACTORY = "mail.smtp.socketFactory.class";
    private static final String SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
    private static final String SOCKET_BOOLEAN = "false";
    private static final String PORT_SMTP = "mail.smtp.socketFactory.fallback";
    private static final String PORT_NUMBER = "465";
    private static final String PORT_SOCKET = "mail.smtp.socketFactory.port";
    private static final String AUTH_SMTP = "mail.smtp.auth";
    private static final String AUTH_BOOLEAN = "true";
    private static final String MAIL_STORE_PROTOCOL = "mail.store.protocol";
    private static final String STORE_PROTOCOL = "pop3";
    private static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    private static final String TRANSPORT_PROTOCOL = "smtp";


    public EmailsSender(){}

    public void SendEmail(Pedido pedido){


        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty(HOST_SMTP, HOST_SMTP_GMAIL);
        props.setProperty(SOCKET_FACTORY, SSL_FACTORY);
        props.setProperty(SOCKET_FACTORY_FALLBACK, SOCKET_BOOLEAN);
        props.setProperty(PORT_SMTP, PORT_NUMBER);
        props.setProperty(PORT_SOCKET, PORT_NUMBER);
        props.put(AUTH_SMTP, AUTH_BOOLEAN);
        props.put(MAIL_STORE_PROTOCOL,STORE_PROTOCOL);
        props.put(MAIL_TRANSPORT_PROTOCOL, TRANSPORT_PROTOCOL);
        final String username = "proyectomarketplacedad@gmail.com";//
        final String password = "urjc1234";
        try{
            Session session = Session.getDefaultInstance(props,
                    new Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }});

            // -- Create a new message --
            Message msg = new MimeMessage(session);

            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(pedido.getEmail(),false));
            msg.setSubject("¡Enhorabuena "+pedido.getNombreComprador()+ "! Has comprado "+pedido.getNombreProducto() +" en Marketplace.");
            msg.setText("El pedido se enviará a la siguiente dirección: " + pedido.getDireccionDestino());
            Transport.send(msg);
            System.out.println("Message sent.");
        }
        catch (MessagingException e)
        {
            System.out.println("Error causado por: " + e);
        }
    }
}
