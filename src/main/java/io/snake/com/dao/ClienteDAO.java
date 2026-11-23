package io.snake.com.dao;

import io.snake.com.config.ConexionDB;
import io.snake.com.dto.ClienteDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static final int ERR_DUPLICADO = 1062;
    public static final int ERR_EN_USO = 1451;

    public ClienteDTO crear(ClienteDTO dto) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, email, id_ciudad) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dto.getNombre());
            ps.setString(2, dto.getEmail());
            ps.setInt(3, dto.getIdCiudad());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    dto.setIdCliente(rs.getInt(1));
                }
            }
            return dto;
        }
    }

    public List<ClienteDTO> listar() throws SQLException {
        String sql = "SELECT id_cliente, nombre, email, id_ciudad FROM clientes ORDER BY id_cliente";
        List<ClienteDTO> lista = new ArrayList<>();

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClienteDTO dto = new ClienteDTO(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getInt("id_ciudad")
                );
                lista.add(dto);
            }
        }
        return lista;
    }

    public ClienteDTO buscarPorId(int idCliente) throws SQLException {
        String sql = "SELECT id_cliente, nombre, email, id_ciudad FROM clientes WHERE id_cliente = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ClienteDTO(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getInt("id_ciudad")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(ClienteDTO dto) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, email = ?, id_ciudad = ? WHERE id_cliente = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dto.getNombre());
            ps.setString(2, dto.getEmail());
            ps.setInt(3, dto.getIdCiudad());
            ps.setInt(4, dto.getIdCliente());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        }
    }
}
