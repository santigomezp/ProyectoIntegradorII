package co.com.proyectoii.udea.virtualstore.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alan on 12/6/2015.
 */
public class Categoria implements Parcelable {
    /**
     * Identificador de la categoria
     */
    private String id;
    /**
     * Nombre de la categoria
     */
    private String nombre;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
