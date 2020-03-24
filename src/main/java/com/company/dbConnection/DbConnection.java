package com.company.dbConnection;

import pedido.Pedido;

import java.sql.*;

public class DbConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_marketplace";
    private static final String USER = "root";
    private static final String PASS = "malpica28";

    public DbConnection(){ }

    public Pedido FindInDB(int parameter){

        Connection conn = null;
        Statement stmt = null;
        int idProd = -1;
        int idDestinatario = -1;

        Pedido pedido = new Pedido();

        try{

            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            System.out.println("Creating statement for id " + parameter);
            stmt = conn.createStatement();

            String sql = String.format("SELECT * FROM pedido WHERE id=%d", parameter);
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

            sql = String.format("SELECT * FROM producto WHERE id=%d", idProd);
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                pedido.setNombreProducto(rs.getString("titulo"));
            }
            rs.close();

            sql = String.format("SELECT * FROM usuario WHERE id=%d", idDestinatario);
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                pedido.setEmail(rs.getString("correo"));
                pedido.setNombreComprador(rs.getString("nombre_real"));
            }
            rs.close();

        } catch(Exception se){
            se.printStackTrace();
        } finally{
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
}
