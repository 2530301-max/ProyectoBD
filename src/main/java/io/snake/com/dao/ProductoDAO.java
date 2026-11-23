package io.snake.com.dao;

import io.snake.com.config.ConexionDB;
import io.snake.com.dto.ProductoDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public static final int ERR_DUPLICADO = 1062;
    public static final int ERR_EN_USO = 1451;

    public ProductoDTO crear(ProductoDTO dto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, id_categoria, precio_referencia) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dto.getNombre());
            ps.setInt(2, dto.getIdCategoria());
            ps.setDouble(3, dto.getPrecioReferencia());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    dto.setIdProducto(rs.getInt(1));
                }
            }
            return dto;
        }
    }

    public List<ProductoDTO> listar() throws SQLException {
        String sql = "SELECT id_producto, nombre, id_categoria, precio_referencia FROM productos ORDER BY id_producto";
        List<ProductoDTO> lista = new ArrayList<>();

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductoDTO dto = new ProductoDTO(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getInt("id_categoria"),
                        rs.getDouble("precio_referencia")
                );
                lista.add(dto);
            }
        }
        return lista;
    }

    public ProductoDTO buscarPorId(int idProducto) throws SQLException {
        String sql = "SELECT id_producto, nombre, id_categoria, precio_referencia FROM productos WHERE id_producto = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProductoDTO(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getInt("id_categoria"),
                            rs.getDouble("precio_referencia")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(ProductoDTO dto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, id_categoria = ?, precio_referencia = ? WHERE id_producto = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dto.getNombre());
            ps.setInt(2, dto.getIdCategoria());
            ps.setDouble(3, dto.getPrecioReferencia());
            ps.setInt(4, dto.getIdProducto());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        }
    }
}
