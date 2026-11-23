package io.snake.com.dao;

import io.snake.com.config.ConexionDB;
import io.snake.com.dto.VendedorDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAO {

    public static final int ERR_DUPLICADO = 1062;
    public static final int ERR_EN_USO = 1451;

    public VendedorDTO crear(VendedorDTO dto) throws SQLException {
        String sql = "INSERT INTO vendedores (id_vendedor, nombre) VALUES (?, ?)";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dto.getIdVendedor());
            ps.setString(2, dto.getNombre());
            ps.executeUpdate();
            return dto;
        }
    }

    public List<VendedorDTO> listar() throws SQLException {
        String sql = "SELECT id_vendedor, nombre FROM vendedores ORDER BY id_vendedor";
        List<VendedorDTO> lista = new ArrayList<>();

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VendedorDTO dto = new VendedorDTO(
                        rs.getString("id_vendedor"),
                        rs.getString("nombre")
                );
                lista.add(dto);
            }
        }
        return lista;
    }

    public VendedorDTO buscarPorId(String idVendedor) throws SQLException {
        String sql = "SELECT id_vendedor, nombre FROM vendedores WHERE id_vendedor = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new VendedorDTO(
                            rs.getString("id_vendedor"),
                            rs.getString("nombre")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(VendedorDTO dto) throws SQLException {
        String sql = "UPDATE vendedores SET nombre = ? WHERE id_vendedor = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dto.getNombre());
            ps.setString(2, dto.getIdVendedor());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String idVendedor) throws SQLException {
        String sql = "DELETE FROM vendedores WHERE id_vendedor = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idVendedor);
            return ps.executeUpdate() > 0;
        }
    }
}
