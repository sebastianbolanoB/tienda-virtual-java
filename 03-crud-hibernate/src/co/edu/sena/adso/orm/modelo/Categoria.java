package co.edu.sena.adso.orm.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** Entidad Categoria del diagrama de clases, mapeada a la tabla categoria. */
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @Column(name = "id_categoria")
    private Short idCategoria;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    /** Asociación reflexiva: una subcategoría pertenece a una categoría padre. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria_padre")
    private Categoria padre;

    protected Categoria() {
        // Constructor vacío requerido por Hibernate
    }

    public Short getIdCategoria() {
        return idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public Categoria getPadre() {
        return padre;
    }
}
