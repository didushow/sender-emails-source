package com.company;

import com.company.dbConnection.DbConnection;
import pedido.Pedido;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.Socket;
import java.util.Properties;

public class GeneradorRunnable implements Runnable{

    protected Socket clientSocket;
    protected int pedidoId;

    public GeneradorRunnable(Socket clientSocket, int pedidoId){
        this.clientSocket = clientSocket;
        this.pedidoId = pedidoId;
    }

    public GeneradorRunnable(){

    }

    @Override
    public void run() {
        DbConnection dbConnection = new DbConnection();
        Pedido pedido = dbConnection.FindInDB(pedidoId);
        SendEmail(pedido);
    }

    private void SendEmail(Pedido pedido){

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
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
