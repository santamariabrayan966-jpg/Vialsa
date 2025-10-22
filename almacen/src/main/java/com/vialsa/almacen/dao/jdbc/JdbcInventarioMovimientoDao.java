package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.InventarioMovimientoDao;
import com.vialsa.almacen.model.InventarioMovimiento;
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

public class JdbcInventarioMovimientoDao implements InventarioMovimientoDao {

    private static final String INSERT_SQL = "INSERT INTO inventario_movimientos (producto_id, tipo, cantidad, fecha_movimiento, referencia) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_PRODUCTO_SQL = "SELECT id, producto_id, tipo, cantidad, fecha_movimiento, referencia FROM inventario_movimientos WHERE producto_id = ? ORDER BY fecha_movimiento DESC";

    @Override
    public InventarioMovimiento registrarMovimiento(InventarioMovimiento movimiento) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, movimiento.getProductoId());
            statement.setString(2, movimiento.getTipo().name());
            statement.setInt(3, movimiento.getCantidad());
            statement.setTimestamp(4, Timestamp.valueOf(movimiento.getFechaMovimiento()));
            statement.setString(5, movimiento.getReferencia());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    movimiento.setId(keys.getLong(1));
                }
            }
            return movimiento;
        } catch (SQLException e) {
            throw new DaoException("Error al registrar movimiento de inventario", e);
        }
    }

    @Override
    public List<InventarioMovimiento> buscarPorProducto(Long productoId) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_PRODUCTO_SQL)) {
            statement.setLong(1, productoId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<InventarioMovimiento> movimientos = new ArrayList<>();
                while (resultSet.next()) {
                    movimientos.add(mapRow(resultSet));
                }
                return movimientos;
            }
        } catch (SQLException e) {
            throw new DaoException("Error al consultar los movimientos de inventario", e);
        }
    }

    private InventarioMovimiento mapRow(ResultSet resultSet) throws SQLException {
        InventarioMovimiento movimiento = new InventarioMovimiento();
        movimiento.setId(resultSet.getLong("id"));
        movimiento.setProductoId(resultSet.getLong("producto_id"));
        movimiento.setTipo(InventarioMovimiento.Tipo.valueOf(resultSet.getString("tipo")));
        movimiento.setCantidad(resultSet.getInt("cantidad"));
        Timestamp timestamp = resultSet.getTimestamp("fecha_movimiento");
        LocalDateTime fecha = timestamp != null ? timestamp.toLocalDateTime() : null;
        movimiento.setFechaMovimiento(fecha);
        movimiento.setReferencia(resultSet.getString("referencia"));
        return movimiento;
    }
}
