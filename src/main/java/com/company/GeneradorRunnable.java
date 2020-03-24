package com.company;

import com.company.dbConnection.DbConnection;
import com.company.emailsSender.EmailsSender;
import pedido.Pedido;
import java.net.Socket;


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
        EmailsSender emailsSender = new EmailsSender();
        emailsSender.SendEmail(pedido);
    }


}
