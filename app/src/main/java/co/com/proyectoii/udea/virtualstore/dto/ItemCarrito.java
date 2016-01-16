package co.com.proyectoii.udea.virtualstore.dto;

/**
 * Created by FranciscoJavier on 15/01/2016.
 */
public class ItemCarrito {

    private Producto producto;
    private String Talla;
    private String Color;
    private int Cantidad;

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getTalla() {
        return Talla;
    }

    public void setTalla(String talla) {
        Talla = talla;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }
}
