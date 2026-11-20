package co.edu.sena.adso.orm.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entidad Marca, mapeada a la tabla marca. */
@Entity
@Table(name = "marca")
public class Marca {

    @Id
    @Column(name = "id_marca")
    private Short idMarca;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    protected Marca() {
        // Constructor vacío requerido por Hibernate
    }

    public Short getIdMarca() {
        return idMarca;
    }

    public String getNombre() {
        return nombre;
    }
}
