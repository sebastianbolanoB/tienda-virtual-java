package co.edu.sena.adso.orm.dao;

import co.edu.sena.adso.orm.modelo.Categoria;
import co.edu.sena.adso.orm.modelo.Marca;
import co.edu.sena.adso.orm.modelo.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.SessionFactory;

/**
 * CRUD de productos con Hibernate. Cada método abre una sesión y una transacción con
 * inTransaction / fromTransaction, que hacen commit al terminar o rollback si hay un error.
 * Hibernate 7 usa los métodos de Jakarta Persistence: persist, find, merge y remove
 * (los antiguos save, update y delete del componente formativo ya no existen en esta versión).
 */
public class ProductoDAO {

    private final SessionFactory factory = HibernateUtil.getSessionFactory();

    /** CREATE: registra el producto y devuelve el id generado por MySQL. */
    public Integer crear(short idCategoria, Short idMarca, String sku, String nombre, BigDecimal precio, int stock) {
        return factory.fromTransaction(session -> {
            Categoria categoria = session.getReference(Categoria.class, idCategoria);
            Marca marca = idMarca == null ? null : session.getReference(Marca.class, idMarca);
            Producto producto = new Producto(categoria, marca, sku, nombre, precio, stock);
            session.persist(producto);
            return producto.getIdProducto();
        });
    }

    /** READ: busca por llave primaria (consultarProducto del diagrama). */
    public Producto buscarPorId(int idProducto) {
        return factory.fromTransaction(session -> session.createSelectionQuery(
                        "from Producto p join fetch p.categoria left join fetch p.marca where p.idProducto = :id",
                        Producto.class)
                .setParameter("id", idProducto)
                .uniqueResult());
    }

    /** READ: lista los productos activos de una categoría y sus subcategorías, con HQL. */
    public List<Producto> listarPorCategoria(String nombreCategoria) {
        return factory.fromTransaction(session -> session.createSelectionQuery(
                        "from Producto p join fetch p.categoria c left join c.padre padre "
                                + "where p.activo = true and (c.nombre = :nombre or padre.nombre = :nombre) "
                                + "order by p.precio desc", Producto.class)
                .setParameter("nombre", nombreCategoria)
                .getResultList());
    }

    /** UPDATE: cambia el precio. No se llama a ningún método de guardar: Hibernate detecta el cambio. */
    public void modificarPrecio(int idProducto, BigDecimal nuevoPrecio) {
        factory.inTransaction(session -> session.find(Producto.class, idProducto).modificarPrecio(nuevoPrecio));
    }

    /** UPDATE: entrada (+) o salida (-) de unidades. */
    public void actualizarStock(int idProducto, int cantidad) {
        factory.inTransaction(session -> session.find(Producto.class, idProducto).actualizarStock(cantidad));
    }

    /** DELETE: elimina el producto. Si tiene pedidos asociados, MySQL lo impide por la llave foránea. */
    public boolean eliminar(int idProducto) {
        return factory.fromTransaction(session -> {
            Producto producto = session.find(Producto.class, idProducto);
            if (producto == null) {
                return false;
            }
            session.remove(producto);
            return true;
        });
    }
}
