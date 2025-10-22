package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.PedidoDetalleDao;
import com.vialsa.almacen.model.PedidoDetalle;
import com.vialsa.almacen.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcPedidoDetalleDao implements PedidoDetalleDao {

    private static final String INSERT_SQL = "INSERT INTO pedido_detalles (pedido_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_PEDIDO_SQL = "SELECT id, pedido_id, producto_id, cantidad, precio_unitario FROM pedido_detalles WHERE pedido_id = ?";

    @Override
    public void guardarDetalles(List<PedidoDetalle> detalles) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            for (PedidoDetalle detalle : detalles) {
                statement.setLong(1, detalle.getPedidoId());
                statement.setLong(2, detalle.getProductoId());
                statement.setInt(3, detalle.getCantidad());
                statement.setBigDecimal(4, detalle.getPrecioUnitario());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DaoException("Error al guardar los detalles del pedido", e);
        }
    }

    @Override
    public List<PedidoDetalle> buscarPorPedido(Long pedidoId) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_PEDIDO_SQL)) {
            statement.setLong(1, pedidoId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<PedidoDetalle> detalles = new ArrayList<>();
                while (resultSet.next()) {
                    detalles.add(mapRow(resultSet));
                }
                return detalles;
            }
        } catch (SQLException e) {
            throw new DaoException("Error al consultar los detalles del pedido", e);
        }
    }

    private PedidoDetalle mapRow(ResultSet resultSet) throws SQLException {
        PedidoDetalle detalle = new PedidoDetalle();
        detalle.setId(resultSet.getLong("id"));
        detalle.setPedidoId(resultSet.getLong("pedido_id"));
        detalle.setProductoId(resultSet.getLong("producto_id"));
        detalle.setCantidad(resultSet.getInt("cantidad"));
        detalle.setPrecioUnitario(resultSet.getBigDecimal("precio_unitario"));
        return detalle;
    }
}
