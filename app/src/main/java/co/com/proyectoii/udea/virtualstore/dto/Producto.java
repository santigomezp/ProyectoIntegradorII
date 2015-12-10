package co.com.proyectoii.udea.virtualstore.dto;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Alan on 12/5/2015.
 */
public class Producto  implements Parcelable {

    /**
     * Identificador del producto
     */
    private String id;

    /**
     * Nombre del producto
     */
    private String nombre;

    /**
     * Precio del producto
     */
    private double precio;

    /**
     * Descripcion del producto
     */
    private String descripcion;

    /**
     * Numero de productos ingresados en un carrito
     */
    private int cantidad;

    /**
     * Disponibilidad del producto en base de datos
     */
    private double stock;

    /**
     * Id de la categoria a la que pertenece el producto
     */
    private String idCategoria;

    /**
     * Identificador del icono por defecto
     */
    private String iconoPorDefecto;

    /**
     * Galeria de imagenes
     */
    private ArrayList<String> imagenes;

    /**
     * Listado de atributos
     */
    private String atributos;

    private byte[] iconData;

    public Producto() {
    }


    public Producto(String id, String nombre, double precio, String descripcion, int cantidad,
                    double stock, String idCategoria, String iconoPorDefecto) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.stock = stock;
        this.idCategoria = idCategoria;
        this.iconoPorDefecto = iconoPorDefecto;
    }

    public Producto(Parcel in){
        this.id = in.readString();
        this.nombre = in.readString();
        this.precio = in.readDouble();
        this.descripcion = in.readString();
        this.cantidad = in.readInt();
        this.stock = in.readDouble();
        this.idCategoria = in.readString();
        this.iconoPorDefecto = in.readString();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return this.precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return this.descripcion.replace("&nbsp;","").trim().replaceAll("\\<.*?>","");
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getStock() {
        return stock;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return this.idCategoria;
    }

    public void setCategoria(String categoria) {
        this.idCategoria  = categoria;
    }

    public String getIconoPorDefecto() {
        return iconoPorDefecto;
    }

    public void setIconoPorDefecto(String iconoPorDefecto) {
        this.iconoPorDefecto = iconoPorDefecto;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }

    public String getAtributos() {
        return atributos;
    }

    public void setAtributos(String atributos) {
        this.atributos = atributos;
    }

    public byte[] getIconData() {
        return iconData;
    }

    public void setIconData(byte[] iconData) {
        this.iconData = iconData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nombre );
        dest.writeDouble(this.precio);
        dest.writeString(this.descripcion);
        dest.writeInt(this.cantidad);
        dest.writeDouble(this.stock);
        dest.writeString(this.idCategoria );
        dest.writeString(this.iconoPorDefecto );

    }
    public static final Parcelable.Creator<Producto> CREATOR = new Parcelable.Creator<Producto>() {
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };
    
}
