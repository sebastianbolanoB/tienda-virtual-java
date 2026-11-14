package co.edu.sena.adso.tienda.dao;

import co.edu.sena.adso.tienda.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Acceso a la tabla producto. Implementa los métodos del diagrama de clases:
 * consultarProducto(), modificarPrecio() (dentro de actualizar) y actualizarStock().
 * Todas las consultas con datos del usuario usan PreparedStatement.
 */
public class ProductoDAO {

    private static final String SELECT_BASE = "SELECT p.*, c.nombre AS categoria, m.nombre AS marca "
            + "FROM producto p JOIN categoria c ON c.id_categoria = p.id_categoria "
            + "LEFT JOIN marca m ON m.id_marca = p.id_marca ";

    /** Solo se aceptan estos valores en ORDER BY; así no se puede inyectar SQL por el parámetro orden. */
    private static final Map<String, String> ORDENES = Map.of(
            "nombre", "p.nombre ASC",
            "precio_asc", "p.precio ASC, p.nombre",
            "precio_desc", "p.precio DESC, p.nombre",
            "stock", "p.stock ASC, p.nombre");

    /**
     * Explorar catálogo con filtros: texto en nombre o SKU, categoría (incluye sus subcategorías)
     * y orden. Si soloActivos es true no muestra los productos desactivados.
     */
    public List<Producto> buscar(String texto, Integer idCategoria, String orden, boolean soloActivos)
            throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT_BASE).append("WHERE 1 = 1 ");
        List<Object> parametros = new ArrayList<>();
        if (texto != null && !texto.isBlank()) {
            sql.append("AND (p.nombre LIKE ? OR p.sku LIKE ?) ");
            String patron = "%" + texto.trim() + "%";
            parametros.add(patron);
            parametros.add(patron);
        }
        if (idCategoria != null) {
            sql.append("AND (p.id_categoria = ? OR c.id_categoria_padre = ?) ");
            parametros.add(idCategoria);
            parametros.add(idCategoria);
        }
        if (soloActivos) {
            sql.append("AND p.activo = 1 ");
        }
        String criterio = orden == null ? null : ORDENES.get(orden);
        sql.append("ORDER BY ").append(criterio == null ? ORDENES.get("nombre") : criterio);

        List<Producto> lista = new ArrayList<>();
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(leer(rs));
                }
            }
        }
        return lista;
    }

    /** consultarProducto(): devuelve el producto o null si no existe. */
    public Producto consultar(int idProducto) throws SQLException {
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + "WHERE p.id_producto = ?")) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? leer(rs) : null;
            }
        }
    }

    /** Registra un producto nuevo y devuelve el id generado. */
    public int insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO producto (id_categoria, id_marca, sku, nombre, descripcion, precio, "
                + "stock, stock_minimo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            asignar(ps, p);
            ps.executeUpdate();
            try (ResultSet llaves = ps.getGeneratedKeys()) {
                llaves.next();
                return llaves.getInt(1);
            }
        }
    }

    /** Modifica los datos del producto, incluido el precio (modificarPrecio). */
    public int actualizar(Producto p) throws SQLException {
        String sql = "UPDATE producto SET id_categoria = ?, id_marca = ?, sku = ?, nombre = ?, "
                + "descripcion = ?, precio = ?, stock = ?, stock_minimo = ? WHERE id_producto = ?";
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            asignar(ps, p);
            ps.setInt(9, p.getIdProducto());
            return ps.executeUpdate();
        }
    }

    /** actualizarStock(): suma (entrada) o resta (salida) unidades sin dejar el stock negativo. */
    public int actualizarStock(int idProducto, int cantidad) throws SQLException {
        String sql = "UPDATE producto SET stock = stock + ? WHERE id_producto = ? AND stock + ? >= 0";
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            return ps.executeUpdate();
        }
    }

    /** Activa o desactiva el producto (borrado lógico). */
    public int cambiarEstado(int idProducto, boolean activo) throws SQLException {
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE producto SET activo = ? WHERE id_producto = ?")) {
            ps.setBoolean(1, activo);
            ps.setInt(2, idProducto);
            return ps.executeUpdate();
        }
    }

    private static void asignar(PreparedStatement ps, Producto p) throws SQLException {
        ps.setInt(1, p.getIdCategoria());
        if (p.getIdMarca() == null) {
            ps.setNull(2, Types.SMALLINT);
        } else {
            ps.setInt(2, p.getIdMarca());
        }
        ps.setString(3, p.getSku());
        ps.setString(4, p.getNombre());
        ps.setString(5, p.getDescripcion());
        ps.setBigDecimal(6, p.getPrecio());
        ps.setInt(7, p.getStock());
        ps.setInt(8, p.getStockMinimo());
    }

    private static Producto leer(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        int idMarca = rs.getInt("id_marca");
        p.setIdMarca(rs.wasNull() ? null : idMarca);
        p.setSku(rs.getString("sku"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setStockMinimo(rs.getInt("stock_minimo"));
        p.setActivo(rs.getBoolean("activo"));
        p.setCategoria(rs.getString("categoria"));
        p.setMarca(rs.getString("marca"));
        return p;
    }
}
