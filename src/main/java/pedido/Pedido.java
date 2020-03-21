package pedido;

import java.io.Serializable;
import java.util.Date;

public class Pedido implements Serializable {

    private long id;
    private double precio;
    private String direccionDestino;
    private Date fecha;
    private String nombreProducto;
    private String email;
    private String nombreComprador;

    public Pedido() {
        this.id = -1;
        this.precio = -1;
        this.direccionDestino = "";
        this.fecha = null;
        this.nombreProducto = "";
        this.email = "";
        this.nombreComprador ="";
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public double getPrecio() {
        return precio;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getEmail() {
        return email;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }
}
