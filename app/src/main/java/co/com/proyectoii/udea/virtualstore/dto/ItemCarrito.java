package co.com.proyectoii.udea.virtualstore.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by FranciscoJavier on 15/01/2016.
 */
public class ItemCarrito implements Parcelable{

    private Producto producto;
    private String talla;
    private String color;
    private int cantidad;
    private double precio;

    public ItemCarrito(){};

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        color = color;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public ItemCarrito(Parcel in){
        this.producto = in.readParcelable(Producto.class.getClassLoader());;
        this.cantidad = in.readInt();
        this.color = in.readString();
        this.talla = in.readString();
        this.precio = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.producto, flags);
        dest.writeInt(this.cantidad);
        dest.writeString(this.color);
        dest.writeString(this.talla);
        dest.writeDouble(this.precio);
      }
    public static final Parcelable.Creator<ItemCarrito> CREATOR = new Parcelable.Creator<ItemCarrito>() {
        public ItemCarrito createFromParcel(Parcel in) {
            return new ItemCarrito(in);
        }

        public ItemCarrito[] newArray(int size) {
            return new ItemCarrito[size];
        }
    };
}
