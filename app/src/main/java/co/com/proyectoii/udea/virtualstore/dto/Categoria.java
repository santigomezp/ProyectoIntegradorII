package co.com.proyectoii.udea.virtualstore.dto;

/**
 * Created by Alan on 12/6/2015.
 */
public class Categoria {
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
}
