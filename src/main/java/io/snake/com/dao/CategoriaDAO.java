package io.snake.com.dao;

import io.snake.com.config.ConexionDB;
import io.snake.com.dto.CategoriaDTO;
import io.snake.com.models.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public static final int ERR_DUPLICADO = 1062;
    public static final int ERR_EN_USO = 1451;

    public CategoriaDTO crear(CategoriaDTO dto) throws SQLException {
        Categoria entidad = mapearAEntidad(dto);
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entidad.getNombre());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setIdCategoria(rs.getInt(1));
                }
            }
            return mapearADTO(entidad);
        }
    }

    public List<CategoriaDTO> listar() throws SQLException {
        String sql = "SELECT id_categoria, nombre FROM categorias ORDER BY id_categoria";
        List<CategoriaDTO> lista = new ArrayList<>();

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Categoria entidad = new Categoria(rs.getInt("id_categoria"), rs.getString("nombre"));
                lista.add(mapearADTO(entidad));
            }
        }
        return lista;
    }

    public CategoriaDTO buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_categoria, nombre FROM categorias WHERE id_categoria = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria entidad = new Categoria(rs.getInt("id_categoria"), rs.getString("nombre"));
                    return mapearADTO(entidad);
                }
            }
        }
        return null;
    }

    public boolean actualizar(CategoriaDTO dto) throws SQLException {
        Categoria entidad = mapearAEntidad(dto);
        String sql = "UPDATE categorias SET nombre = ? WHERE id_categoria = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setInt(2, entidad.getIdCategoria());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";
        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private CategoriaDTO mapearADTO(Categoria entidad) {
        return new CategoriaDTO(entidad.getIdCategoria(), entidad.getNombre());
    }

    private Categoria mapearAEntidad(CategoriaDTO dto) {
        return new Categoria(dto.getId(), dto.getNombre());
    }
}
