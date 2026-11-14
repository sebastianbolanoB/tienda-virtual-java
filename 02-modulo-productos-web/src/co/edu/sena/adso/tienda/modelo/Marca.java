package co.edu.sena.adso.tienda.modelo;

/** Marca de un producto (tabla marca). */
public class Marca {

    private int idMarca;
    private String nombre;

    public Marca(int idMarca, String nombre) {
        this.idMarca = idMarca;
        this.nombre = nombre;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public String getNombre() {
        return nombre;
    }
}
