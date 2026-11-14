package co.edu.sena.adso.jdbc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

/**
 * Operaciones CRUD sobre la tabla usuario.
 * Cada método abre su conexión y la cierra con try-with-resources.
 */
public class UsuarioDAO {

    /** Crear: inserta un usuario y devuelve el id generado. */
    public int insertar(String username, String clave) throws SQLException {
        String sql = "INSERT INTO usuario (username, user_password) VALUES (?, ?)";
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, hash(clave));
            ps.executeUpdate();
            try (ResultSet llaves = ps.getGeneratedKeys()) {
                llaves.next();
                return llaves.getInt(1);
            }
        }
    }

    /** Leer: pasos f, g y h con Statement, executeQuery y ResultSet. */
    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = Conexion.obtener();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT user_id, username, user_password FROM usuario ORDER BY user_id")) {
            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("user_id"), rs.getString("username"),
                        rs.getString("user_password")));
            }
        }
        return lista;
    }

    /** Actualizar: cambia la contraseña de un usuario. Devuelve las filas afectadas. */
    public int cambiarClave(String username, String claveNueva) throws SQLException {
        String sql = "UPDATE usuario SET user_password = ? WHERE username = ?";
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hash(claveNueva));
            ps.setString(2, username);
            return ps.executeUpdate();
        }
    }

    /** Valida usuario y contraseña comparando el hash guardado. */
    public boolean validar(String username, String clave) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE username = ? AND user_password = ?";
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(clave));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 1;
            }
        }
    }

    /** Eliminar: borra un usuario por su nombre. Devuelve las filas afectadas. */
    public int eliminar(String username) throws SQLException {
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement("DELETE FROM usuario WHERE username = ?")) {
            ps.setString(1, username);
            return ps.executeUpdate();
        }
    }

    /** La contraseña nunca se guarda en texto plano: se guarda su hash SHA-256. */
    private static String hash(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(texto.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
