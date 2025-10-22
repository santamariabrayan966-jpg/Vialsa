package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.ProductoDao;
import com.vialsa.almacen.model.Producto;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcProductoDao implements ProductoDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Producto> mapper = (rs, rowNum) -> {
        Producto producto = new Producto();
        producto.setId(rs.getLong("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        producto.setStock(rs.getInt("stock"));
        return producto;
    };

    public JdbcProductoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Producto> findAll() {
        try {
            return jdbcTemplate.query("SELECT id, nombre, descripcion, precio, stock FROM productos ORDER BY nombre", mapper);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar productos", ex);
        }
    }

    @Override
    public Optional<Producto> findById(Long id) {
        try {
            List<Producto> productos = jdbcTemplate.query("SELECT id, nombre, descripcion, precio, stock FROM productos WHERE id = ?", mapper, id);
            return productos.stream().findFirst();
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar producto", ex);
        }
    }

    @Override
    public Producto save(Producto producto) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO productos (nombre, descripcion, precio, stock) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getDescripcion());
                ps.setBigDecimal(3, producto.getPrecio());
                ps.setInt(4, producto.getStock());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) {
                producto.setId(key.longValue());
            }
            return producto;
        } catch (DataAccessException ex) {
            throw new DaoException("Error al registrar producto", ex);
        }
    }

    @Override
    public void update(Producto producto) {
        try {
            jdbcTemplate.update("UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ? WHERE id = ?",
                    producto.getNombre(), producto.getDescripcion(), producto.getPrecio(), producto.getStock(), producto.getId());
        } catch (DataAccessException ex) {
            throw new DaoException("Error al actualizar producto", ex);
        }
    }

    @Override
    public void updateStock(Long productoId, int nuevoStock) {
        try {
            jdbcTemplate.update("UPDATE productos SET stock = ? WHERE id = ?", nuevoStock, productoId);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al actualizar stock", ex);
        }
    }
}
