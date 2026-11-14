package co.edu.sena.adso.tienda.dao;

import co.edu.sena.adso.tienda.modelo.Categoria;
import co.edu.sena.adso.tienda.modelo.Marca;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Consultas de las listas de categorías y marcas que usan los formularios. */
public class CategoriaDAO {

    public List<Categoria> listarCategorias() throws SQLException {
        String sql = "SELECT c.id_categoria, c.nombre, p.nombre AS padre FROM categoria c "
                + "LEFT JOIN categoria p ON p.id_categoria = c.id_categoria_padre "
                + "ORDER BY COALESCE(p.nombre, c.nombre), c.id_categoria_padre IS NOT NULL, c.nombre";
        List<Categoria> lista = new ArrayList<>();
        try (Connection con = Conexion.obtener();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nombre"),
                        rs.getString("padre")));
            }
        }
        return lista;
    }

    public List<Marca> listarMarcas() throws SQLException {
        List<Marca> lista = new ArrayList<>();
        try (Connection con = Conexion.obtener();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_marca, nombre FROM marca ORDER BY nombre")) {
            while (rs.next()) {
                lista.add(new Marca(rs.getInt("id_marca"), rs.getString("nombre")));
            }
        }
        return lista;
    }
}
