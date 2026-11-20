package co.edu.sena.adso.orm;

import co.edu.sena.adso.orm.dao.HibernateUtil;
import co.edu.sena.adso.orm.dao.ProductoDAO;
import co.edu.sena.adso.orm.modelo.Producto;
import java.math.BigDecimal;
import org.hibernate.Version;

/** Programa de prueba del CRUD de productos con Hibernate. */
public class App {

    public static void main(String[] args) {
        System.out.println("Hibernate ORM " + Version.getVersionString());
        ProductoDAO dao = new ProductoDAO();
        try {
            titulo("CREATE - persist()");
            Integer id = dao.crear((short) 3, (short) 2, "PER-003", "Cámara web Full HD", new BigDecimal("129900"), 10);
            System.out.println("Producto creado con id " + id);

            titulo("READ - consulta por id y HQL por categoría");
            System.out.println(dao.buscarPorId(id));
            System.out.println("Productos de la categoría Tecnología (incluye subcategorías):");
            dao.listarPorCategoria("Tecnología").forEach(System.out::println);

            titulo("UPDATE - modificarPrecio() y actualizarStock()");
            dao.modificarPrecio(id, new BigDecimal("119900"));
            dao.actualizarStock(id, -4);
            System.out.println(dao.buscarPorId(id));

            titulo("Regla de negocio: salida mayor que el stock");
            try {
                dao.actualizarStock(id, -50);
            } catch (IllegalArgumentException e) {
                System.out.println("Rechazado: " + e.getMessage());
            }

            titulo("DELETE - remove()");
            System.out.println("Eliminado: " + dao.eliminar(id));
            Producto despues = dao.buscarPorId(id);
            System.out.println("Consulta después de eliminar: " + (despues == null ? "no existe" : despues));
        } finally {
            HibernateUtil.cerrar();
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("== " + texto + " ==");
    }
}
