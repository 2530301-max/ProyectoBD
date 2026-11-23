package io.snake.com.dao;

import io.snake.com.config.ConexionDB;
import io.snake.com.dto.VentaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public VentaDTO crear(VentaDTO dto) throws SQLException {
        String sql = "INSERT INTO ventas (fecha_venta, id_cliente, id_vendedor, total_venta) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, java.sql.Date.valueOf(dto.getFechaVenta()));
            ps.setInt(2, dto.getIdCliente());
            ps.setString(3, dto.getIdVendedor());
            ps.setDouble(4, dto.getTotalVenta());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    dto.setIdVenta(rs.getInt(1));
                }
            }
            return dto;
        }
    }

    public List<VentaDTO> listar() throws SQLException {
        String sql = "SELECT id_venta, fecha_venta, id_cliente, id_vendedor, total_venta FROM ventas ORDER BY id_venta";
        List<VentaDTO> lista = new ArrayList<>();

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VentaDTO dto = new VentaDTO(
                        rs.getInt("id_venta"),
                        rs.getDate("fecha_venta").toLocalDate(),
                        rs.getInt("id_cliente"),
                        rs.getString("id_vendedor"),
                        rs.getDouble("total_venta")
                );
                lista.add(dto);
            }
        }
        return lista;
    }

    public VentaDTO buscarPorId(int idVenta) throws SQLException {
        String sql = "SELECT id_venta, fecha_venta, id_cliente, id_vendedor, total_venta FROM ventas WHERE id_venta = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new VentaDTO(
                            rs.getInt("id_venta"),
                            rs.getDate("fecha_venta").toLocalDate(),
                            rs.getInt("id_cliente"),
                            rs.getString("id_vendedor"),
                            rs.getDouble("total_venta")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(VentaDTO dto) throws SQLException {
        String sql = "UPDATE ventas SET fecha_venta = ?, id_cliente = ?, id_vendedor = ?, total_venta = ? WHERE id_venta = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(dto.getFechaVenta()));
            ps.setInt(2, dto.getIdCliente());
            ps.setString(3, dto.getIdVendedor());
            ps.setDouble(4, dto.getTotalVenta());
            ps.setInt(5, dto.getIdVenta());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idVenta) throws SQLException {
        String sql = "DELETE FROM ventas WHERE id_venta = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            return ps.executeUpdate() > 0;
        }
    }
}
