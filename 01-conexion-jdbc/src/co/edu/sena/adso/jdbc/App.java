package co.edu.sena.adso.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

/** Programa de prueba de la conexión JDBC y del CRUD de la tabla usuario. */
public class App {

    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();
        try {
            probarConexion();

            titulo("CREATE - INSERT de tres usuarios");
            System.out.println("Insertado sebas  con id " + dao.insertar("sebas", "Clave123"));
            System.out.println("Insertado laura  con id " + dao.insertar("laura", "Sena2026"));
            System.out.println("Insertado carlos con id " + dao.insertar("carlos", "Java21"));
            mostrar(dao.listar());

            titulo("Validación de usuario y contraseña");
            System.out.println("sebas / Clave123 -> " + dao.validar("sebas", "Clave123"));
            System.out.println("sebas / otra     -> " + dao.validar("sebas", "otra"));

            titulo("UPDATE - cambio de contraseña de laura");
            System.out.println("Filas actualizadas: " + dao.cambiarClave("laura", "NuevaClave1"));
            System.out.println("laura / NuevaClave1 -> " + dao.validar("laura", "NuevaClave1"));

            titulo("DELETE - eliminar a carlos");
            System.out.println("Filas eliminadas: " + dao.eliminar("carlos"));
            mostrar(dao.listar());

            titulo("Prueba de error: username repetido");
            try {
                dao.insertar("sebas", "x");
            } catch (SQLException e) {
                System.out.println("MySQL rechazó el registro: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private static void probarConexion() throws SQLException {
        titulo("Conexión a la base de datos");
        try (Connection con = Conexion.obtener()) {
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("URL:      " + Conexion.getUrl());
            System.out.println("Servidor: " + meta.getDatabaseProductName() + " "
                    + meta.getDatabaseProductVersion());
            System.out.println("Driver:   " + meta.getDriverName() + " " + meta.getDriverVersion());
            System.out.println("Usuario:  " + meta.getUserName());
            System.out.println("Conexión abierta: " + !con.isClosed());
        }
    }

    private static void mostrar(List<Usuario> usuarios) {
        System.out.println("ID   USERNAME     USER_PASSWORD (hash)");
        usuarios.forEach(System.out::println);
        System.out.println("Total: " + usuarios.size() + " registro(s)");
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("== " + texto + " ==");
    }
}
