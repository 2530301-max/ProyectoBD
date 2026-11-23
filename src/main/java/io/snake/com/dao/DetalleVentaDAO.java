package io.snake.com.dao;

import io.snake.com.config.ConexionDB;
import io.snake.com.dto.DetalleVentaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {

    public static final int ERR_DUPLICADO = 1062;
    public static final int ERR_EN_USO = 1451;

    public DetalleVentaDTO crear(DetalleVentaDTO dto) throws SQLException {
        String sql = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dto.getIdVenta());
            ps.setInt(2, dto.getIdProducto());
            ps.setInt(3, dto.getCantidad());
            ps.executeUpdate();
            return dto;
        }
    }

    public List<DetalleVentaDTO> listar() throws SQLException {
        String sql = "SELECT id_venta, id_producto, cantidad FROM detalle_venta ORDER BY id_venta, id_producto";
        List<DetalleVentaDTO> lista = new ArrayList<>();

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DetalleVentaDTO dto = new DetalleVentaDTO(
                        rs.getInt("id_venta"),
                        rs.getInt("id_producto"),
                        rs.getInt("cantidad")
                );
                lista.add(dto);
            }
        }
        return lista;
    }

    public List<DetalleVentaDTO> buscarPorVenta(int idVenta) throws SQLException {
        String sql = "SELECT id_venta, id_producto, cantidad FROM detalle_venta WHERE id_venta = ? ORDER BY id_producto";
        List<DetalleVentaDTO> lista = new ArrayList<>();

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DetalleVentaDTO(
                            rs.getInt("id_venta"),
                            rs.getInt("id_producto"),
                            rs.getInt("cantidad")
                    ));
                }
            }
        }
        return lista;
    }

    public DetalleVentaDTO buscarPorId(int idVenta, int idProducto) throws SQLException {
        String sql = "SELECT id_venta, id_producto, cantidad FROM detalle_venta WHERE id_venta = ? AND id_producto = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            ps.setInt(2, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DetalleVentaDTO(
                            rs.getInt("id_venta"),
                            rs.getInt("id_producto"),
                            rs.getInt("cantidad")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(DetalleVentaDTO dto) throws SQLException {
        String sql = "UPDATE detalle_venta SET cantidad = ? WHERE id_venta = ? AND id_producto = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dto.getCantidad());
            ps.setInt(2, dto.getIdVenta());
            ps.setInt(3, dto.getIdProducto());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idVenta, int idProducto) throws SQLException {
        String sql = "DELETE FROM detalle_venta WHERE id_venta = ? AND id_producto = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;
        }
    }
}
