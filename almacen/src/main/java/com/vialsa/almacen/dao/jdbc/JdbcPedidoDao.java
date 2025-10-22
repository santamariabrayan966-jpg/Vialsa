package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.PedidoDao;
import com.vialsa.almacen.model.Pedido;
import com.vialsa.almacen.model.PedidoEstado;
import com.vialsa.almacen.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPedidoDao implements PedidoDao {

    private static final String INSERT_SQL = "INSERT INTO pedidos (cliente_id, fecha_creacion, estado) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE pedidos SET cliente_id = ?, fecha_creacion = ?, estado = ? WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT id, cliente_id, fecha_creacion, estado FROM pedidos WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, cliente_id, fecha_creacion, estado FROM pedidos ORDER BY fecha_creacion DESC";

    @Override
    public Pedido guardar(Pedido pedido) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, pedido.getClienteId());
            statement.setTimestamp(2, Timestamp.valueOf(pedido.getFechaCreacion()));
            statement.setString(3, pedido.getEstado().name());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    pedido.setId(keys.getLong(1));
                }
            }
            return pedido;
        } catch (SQLException e) {
            throw new DaoException("Error al registrar el pedido", e);
        }
    }

    @Override
    public void actualizar(Pedido pedido) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setLong(1, pedido.getClienteId());
            statement.setTimestamp(2, Timestamp.valueOf(pedido.getFechaCreacion()));
            statement.setString(3, pedido.getEstado().name());
            statement.setLong(4, pedido.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error al actualizar el pedido", e);
        }
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) throws DaoException {
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
            throw new DaoException("Error al buscar pedido", e);
        }
    }

    @Override
    public List<Pedido> buscarTodos() throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Pedido> pedidos = new ArrayList<>();
            while (resultSet.next()) {
                pedidos.add(mapRow(resultSet));
            }
            return pedidos;
        } catch (SQLException e) {
            throw new DaoException("Error al listar pedidos", e);
        }
    }

    private Pedido mapRow(ResultSet resultSet) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(resultSet.getLong("id"));
        pedido.setClienteId(resultSet.getLong("cliente_id"));
        Timestamp timestamp = resultSet.getTimestamp("fecha_creacion");
        LocalDateTime fecha = timestamp != null ? timestamp.toLocalDateTime() : null;
        pedido.setFechaCreacion(fecha);
        pedido.setEstado(PedidoEstado.valueOf(resultSet.getString("estado")));
        return pedido;
    }
}
