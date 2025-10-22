package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.ClienteDao;
import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.model.Cliente;
import com.vialsa.almacen.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcClienteDao implements ClienteDao {

    private static final String INSERT_SQL = "INSERT INTO clientes (nombre, direccion, telefono, email) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, nombre, direccion, telefono, email FROM clientes WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, nombre, direccion, telefono, email FROM clientes ORDER BY nombre";
    private static final String UPDATE_SQL = "UPDATE clientes SET nombre = ?, direccion = ?, telefono = ?, email = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM clientes WHERE id = ?";

    @Override
    public Cliente guardar(Cliente cliente) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getDireccion());
            statement.setString(3, cliente.getTelefono());
            statement.setString(4, cliente.getEmail());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    cliente.setId(keys.getLong(1));
                }
            }
            return cliente;
        } catch (SQLException e) {
            throw new DaoException("Error al guardar el cliente", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) throws DaoException {
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
            throw new DaoException("Error al buscar cliente", e);
        }
    }

    @Override
    public List<Cliente> buscarTodos() throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Cliente> clientes = new ArrayList<>();
            while (resultSet.next()) {
                clientes.add(mapRow(resultSet));
            }
            return clientes;
        } catch (SQLException e) {
            throw new DaoException("Error al listar clientes", e);
        }
    }

    @Override
    public void actualizar(Cliente cliente) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getDireccion());
            statement.setString(3, cliente.getTelefono());
            statement.setString(4, cliente.getEmail());
            statement.setLong(5, cliente.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error al actualizar el cliente", e);
        }
    }

    @Override
    public void eliminar(Long id) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error al eliminar el cliente", e);
        }
    }

    private Cliente mapRow(ResultSet resultSet) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(resultSet.getLong("id"));
        cliente.setNombre(resultSet.getString("nombre"));
        cliente.setDireccion(resultSet.getString("direccion"));
        cliente.setTelefono(resultSet.getString("telefono"));
        cliente.setEmail(resultSet.getString("email"));
        return cliente;
    }
}
