package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.VentaDao;
import com.vialsa.almacen.model.Venta;
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

public class JdbcVentaDao implements VentaDao {

    private static final String INSERT_SQL = "INSERT INTO ventas (cliente_id, fecha_venta, total) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, cliente_id, fecha_venta, total FROM ventas WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, cliente_id, fecha_venta, total FROM ventas ORDER BY fecha_venta DESC";

    @Override
    public Venta guardar(Venta venta) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, venta.getClienteId());
            statement.setTimestamp(2, Timestamp.valueOf(venta.getFechaVenta()));
            statement.setDouble(3, venta.getTotal());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    venta.setId(keys.getLong(1));
                }
            }
            return venta;
        } catch (SQLException e) {
            throw new DaoException("Error al guardar la venta", e);
        }
    }

    @Override
    public Optional<Venta> buscarPorId(Long id) throws DaoException {
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
            throw new DaoException("Error al buscar la venta", e);
        }
    }

    @Override
    public List<Venta> buscarTodos() throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Venta> ventas = new ArrayList<>();
            while (resultSet.next()) {
                ventas.add(mapRow(resultSet));
            }
            return ventas;
        } catch (SQLException e) {
            throw new DaoException("Error al listar ventas", e);
        }
    }

    private Venta mapRow(ResultSet resultSet) throws SQLException {
        Venta venta = new Venta();
        venta.setId(resultSet.getLong("id"));
        venta.setClienteId(resultSet.getLong("cliente_id"));
        Timestamp timestamp = resultSet.getTimestamp("fecha_venta");
        LocalDateTime fecha = timestamp != null ? timestamp.toLocalDateTime() : null;
        venta.setFechaVenta(fecha);
        venta.setTotal(resultSet.getDouble("total"));
        return venta;
    }
}
