package co.edu.sena.adso.orm.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Producto del diagrama de clases, mapeada a la tabla producto.
 * Los métodos modificarPrecio() y actualizarStock() del diagrama quedan en la propia entidad:
 * Hibernate detecta el cambio (dirty checking) y genera el UPDATE al confirmar la transacción.
 */
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // AUTO_INCREMENT de MySQL
    @Column(name = "id_producto")
    private Integer idProducto;

    /** Muchos productos pertenecen a una categoría (llave foránea id_categoria). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    /** La marca es opcional (id_marca admite NULL). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca")
    private Marca marca;

    @Column(name = "sku", nullable = false, unique = true, length = 20)
    private String sku;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio", nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "stock_minimo", nullable = false)
    private int stockMinimo = 5;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    /** La fecha la asigna MySQL (DEFAULT CURRENT_TIMESTAMP); Hibernate no la escribe. */
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    protected Producto() {
        // Constructor vacío requerido por Hibernate
    }

    public Producto(Categoria categoria, Marca marca, String sku, String nombre, BigDecimal precio, int stock) {
        this.categoria = categoria;
        this.marca = marca;
        this.sku = sku;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    /** modificarPrecio() del diagrama de clases. */
    public void modificarPrecio(BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        this.precio = nuevoPrecio;
    }

    /** actualizarStock() del diagrama de clases: positivo = entrada, negativo = salida. */
    public void actualizarStock(int cantidad) {
        if (stock + cantidad < 0) {
            throw new IllegalArgumentException("No hay unidades suficientes: stock " + stock + ", salida " + (-cantidad));
        }
        this.stock += cantidad;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public String getSku() {
        return sku;
    }

    public String getNombre() {
        return nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public boolean isActivo() {
        return activo;
    }

    @Override
    public String toString() {
        return String.format("%-3d %-8s %-36s %-12s %10s %5d", idProducto, sku, nombre, categoria.getNombre(),
                precio.toPlainString(), stock);
    }
}
