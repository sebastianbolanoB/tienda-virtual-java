package co.edu.sena.adso.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Crea las conexiones a la base de datos por medio de JDBC.
 * Los datos de conexión se leen del archivo db.properties.
 */
public final class Conexion {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String ARCHIVO_CONFIG = "db.properties";

    private static String url;
    private static String usuario;
    private static String clave;

    private Conexion() {
    }

    /** Paso d: inicializa el driver y carga los datos de conexión una sola vez. */
    private static synchronized void inicializar() throws SQLException {
        if (url != null) {
            return;
        }
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC en el classpath: " + DRIVER, e);
        }
        Properties config = new Properties();
        try (InputStream entrada = new FileInputStream(ARCHIVO_CONFIG)) {
            config.load(entrada);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer " + ARCHIVO_CONFIG, e);
        }
        url = config.getProperty("db.url");
        usuario = config.getProperty("db.usuario");
        clave = config.getProperty("db.clave");
    }

    /** Paso e: crea un objeto Connection por medio del DriverManager. */
    public static Connection obtener() throws SQLException {
        inicializar();
        return DriverManager.getConnection(url, usuario, clave);
    }

    public static String getUrl() {
        return url;
    }
}
