package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.ProductoDao;
import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcProductoDao implements ProductoDao {

    private static final String INSERT_SQL = "INSERT INTO productos (nombre, descripcion, precio_unitario, stock) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, nombre, descripcion, precio_unitario, stock FROM productos WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, nombre, descripcion, precio_unitario, stock FROM productos ORDER BY nombre";
    private static final String UPDATE_SQL = "UPDATE productos SET nombre = ?, descripcion = ?, precio_unitario = ?, stock = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM productos WHERE id = ?";

    @Override
    public Producto guardar(Producto producto) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getDescripcion());
            statement.setBigDecimal(3, producto.getPrecioUnitario());
            statement.setInt(4, producto.getStock());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getLong(1));
                }
            }
            return producto;
        } catch (SQLException e) {
            throw new DaoException("Error al guardar el producto", e);
        }
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Error al buscar producto por id", e);
        }
    }

    @Override
    public List<Producto> buscarTodos() throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Producto> productos = new ArrayList<>();
            while (resultSet.next()) {
                productos.add(mapRow(resultSet));
            }
            return productos;
        } catch (SQLException e) {
            throw new DaoException("Error al listar productos", e);
        }
    }

    @Override
    public void actualizar(Producto producto) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getDescripcion());
            statement.setBigDecimal(3, producto.getPrecioUnitario());
            statement.setInt(4, producto.getStock());
            statement.setLong(5, producto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error al actualizar el producto", e);
        }
    }

    @Override
    public void eliminar(Long id) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error al eliminar el producto", e);
        }
    }

    private Producto mapRow(ResultSet resultSet) throws SQLException {
        Producto producto = new Producto();
        producto.setId(resultSet.getLong("id"));
        producto.setNombre(resultSet.getString("nombre"));
        producto.setDescripcion(resultSet.getString("descripcion"));
        producto.setPrecioUnitario(resultSet.getBigDecimal("precio_unitario"));
        producto.setStock(resultSet.getInt("stock"));
        return producto;
    }
}
