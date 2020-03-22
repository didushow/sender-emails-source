package com.company;

import pedido.Pedido;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.Socket;
import java.sql.*;
import java.util.Properties;

public class GeneradorRunnable implements Runnable{

    protected Socket clientSocket;
    protected int pedidoId;

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/db_marketplace";
    static final String USER = "root";
    static final String PASS = "malpica28";

    public GeneradorRunnable(Socket clientSocket, int pedidoId){
        this.clientSocket = clientSocket;
        this.pedidoId = pedidoId;
    }

    public GeneradorRunnable(){

    }

    @Override
    public void run() {
        int closeConnection = -1;
        Pedido pedido = buscaEnBBDD();

        SendEmail(pedido);
    }

    /*static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[512];
        int len = 0;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
    }*/

    private Pedido buscaEnBBDD(){
        Connection conn = null;
        Statement stmt = null;
        int idProd = -1;
        int idDestinatario = -1;
        Pedido pedido = new Pedido();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            System.out.println("Creating statement for id " + this.pedidoId);
            stmt = conn.createStatement();

            String sql = "SELECT * FROM pedido WHERE id="+this.pedidoId;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                pedido.setId(rs.getLong("id"));
                pedido.setPrecio(rs.getDouble("precio"));
                pedido.setDireccionDestino(rs.getString("direccion_destino"));
                pedido.setFecha(rs.getDate("fecha"));

                idProd = rs.getInt("producto_id");
                idDestinatario = rs.getInt("destinatario_id");
            }
            rs.close();

            sql ="SELECT * FROM producto WHERE id=" + idProd;
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                pedido.setNombreProducto(rs.getString("titulo"));
            }
            rs.close();

            sql ="SELECT * FROM usuario WHERE id=" + idDestinatario;
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                pedido.setEmail(rs.getString("correo"));
                pedido.setNombreComprador(rs.getString("nombre_real"));
            }
            rs.close();

        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return pedido;
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
