package co.edu.sena.adso.tienda.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Crea las conexiones JDBC a tienda_virtual.
 * Es la misma clase de GA7-AA2-EV01, adaptada a la aplicación web: el archivo
 * db.properties se lee desde WEB-INF/classes con el cargador de clases.
 */
public final class Conexion {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String ARCHIVO_CONFIG = "db.properties";

    private static String url;
    private static String usuario;
    private static String clave;

    private Conexion() {
    }

    private static synchronized void inicializar() throws SQLException {
        if (url != null) {
            return;
        }
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC en WEB-INF/lib: " + DRIVER, e);
        }
        Properties config = new Properties();
        try (InputStream entrada = Conexion.class.getClassLoader().getResourceAsStream(ARCHIVO_CONFIG)) {
            if (entrada == null) {
                throw new SQLException("No se encontró " + ARCHIVO_CONFIG + " en WEB-INF/classes");
            }
            config.load(entrada);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer " + ARCHIVO_CONFIG, e);
        }
        url = config.getProperty("db.url");
        usuario = config.getProperty("db.usuario");
        clave = config.getProperty("db.clave");
    }

    public static Connection obtener() throws SQLException {
        inicializar();
        return DriverManager.getConnection(url, usuario, clave);
    }
}
