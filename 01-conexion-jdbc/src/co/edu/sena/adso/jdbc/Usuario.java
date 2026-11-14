package co.edu.sena.adso.jdbc;

/** Representa un registro de la tabla usuario. */
public class Usuario {

    private int userId;
    private String username;
    private String userPassword;

    public Usuario(int userId, String username, String userPassword) {
        this.userId = userId;
        this.username = username;
        this.userPassword = userPassword;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    @Override
    public String toString() {
        return String.format("%-4d %-12s %s...", userId, username, userPassword.substring(0, 16));
    }
}
