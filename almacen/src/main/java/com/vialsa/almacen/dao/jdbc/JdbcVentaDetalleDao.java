package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.VentaDetalleDao;
import com.vialsa.almacen.model.VentaDetalle;
import com.vialsa.almacen.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcVentaDetalleDao implements VentaDetalleDao {

    private static final String INSERT_SQL = "INSERT INTO venta_detalles (venta_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_VENTA_SQL = "SELECT id, venta_id, producto_id, cantidad, precio_unitario FROM venta_detalles WHERE venta_id = ?";

    @Override
    public void guardarDetalles(List<VentaDetalle> detalles) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            for (VentaDetalle detalle : detalles) {
                statement.setLong(1, detalle.getVentaId());
                statement.setLong(2, detalle.getProductoId());
                statement.setInt(3, detalle.getCantidad());
                statement.setBigDecimal(4, detalle.getPrecioUnitario());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DaoException("Error al guardar los detalles de la venta", e);
        }
    }

    @Override
    public List<VentaDetalle> buscarPorVenta(Long ventaId) throws DaoException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_VENTA_SQL)) {
            statement.setLong(1, ventaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<VentaDetalle> detalles = new ArrayList<>();
                while (resultSet.next()) {
                    detalles.add(mapRow(resultSet));
                }
                return detalles;
            }
        } catch (SQLException e) {
            throw new DaoException("Error al consultar los detalles de la venta", e);
        }
    }

    private VentaDetalle mapRow(ResultSet resultSet) throws SQLException {
        VentaDetalle detalle = new VentaDetalle();
        detalle.setId(resultSet.getLong("id"));
        detalle.setVentaId(resultSet.getLong("venta_id"));
        detalle.setProductoId(resultSet.getLong("producto_id"));
        detalle.setCantidad(resultSet.getInt("cantidad"));
        detalle.setPrecioUnitario(resultSet.getBigDecimal("precio_unitario"));
        return detalle;
    }
}
