package co.edu.sena.adso.tienda.modelo;

/** Clase Categoria del diagrama de clases (tabla categoria). */
public class Categoria {

    private int idCategoria;
    private String nombre;
    private String nombrePadre;

    public Categoria(int idCategoria, String nombre, String nombrePadre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.nombrePadre = nombrePadre;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    /** Nombre para las listas: "Tecnología / Audio" cuando es subcategoría. */
    public String getNombreCompleto() {
        return nombrePadre == null ? nombre : nombrePadre + " / " + nombre;
    }
}
