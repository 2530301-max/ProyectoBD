package io.snake.com.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL =
            "jdbc:mariadb://localhost:3306/cafeteria";

    private static String usuario;
    private static String password;

    private ConexionDB() { }

    public static void iniciarSesion(String user, String pass) throws SQLException {
        try (Connection con = DriverManager.getConnection(URL, user, pass)) {
            usuario = user;
            password = pass;
        }
    }

    public static void cerrarSesion() {
        usuario = null;
        password = null;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static Connection obtener() throws SQLException {
        if (usuario == null) {
            throw new SQLException("No hay una sesión iniciada.");
        }
        return DriverManager.getConnection(URL, usuario, password);
    }
}
