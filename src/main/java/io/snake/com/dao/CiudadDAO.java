package io.snake.com.dao;

import io.snake.com.config.ConexionDB;
import io.snake.com.dto.CiudadDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CiudadDAO {

    public static final int ERR_DUPLICADO = 1062;
    public static final int ERR_EN_USO = 1451;

    public CiudadDTO crear(CiudadDTO dto) throws SQLException {
        String sql = "INSERT INTO ciudades (nombre) VALUES (?)";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dto.getNombre());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    dto.setIdCiudad(rs.getInt(1));
                }
            }
            return dto;
        }
    }

    public List<CiudadDTO> listar() throws SQLException {
        String sql = "SELECT id_ciudad, nombre FROM ciudades ORDER BY id_ciudad";
        List<CiudadDTO> lista = new ArrayList<>();

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CiudadDTO dto = new CiudadDTO(
                        rs.getInt("id_ciudad"),
                        rs.getString("nombre")
                );
                lista.add(dto);
            }
        }
        return lista;
    }

    public CiudadDTO buscarPorId(int idCiudad) throws SQLException {
        String sql = "SELECT id_ciudad, nombre FROM ciudades WHERE id_ciudad = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCiudad);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CiudadDTO(
                            rs.getInt("id_ciudad"),
                            rs.getString("nombre")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(CiudadDTO dto) throws SQLException {
        String sql = "UPDATE ciudades SET nombre = ? WHERE id_ciudad = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dto.getNombre());
            ps.setInt(2, dto.getIdCiudad());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idCiudad) throws SQLException {
        String sql = "DELETE FROM ciudades WHERE id_ciudad = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCiudad);
            return ps.executeUpdate() > 0;
        }
    }
}
