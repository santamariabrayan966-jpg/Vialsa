package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.PedidoDetalleDao;
import com.vialsa.almacen.model.PedidoDetalle;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcPedidoDetalleDao implements PedidoDetalleDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPedidoDetalleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void guardarDetalles(List<PedidoDetalle> detalles) {
        try {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO pedido_detalles (pedido_id, producto_id, cantidad, precio_unitario) VALUES (?,?,?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            PedidoDetalle detalle = detalles.get(i);
                            ps.setLong(1, detalle.getPedidoId());
                            ps.setLong(2, detalle.getProductoId());
                            ps.setInt(3, detalle.getCantidad());
                            ps.setBigDecimal(4, detalle.getPrecioUnitario());
                        }

                        @Override
                        public int getBatchSize() {
                            return detalles.size();
                        }
                    }
            );
        } catch (DataAccessException ex) {
            throw new DaoException("Error al registrar detalles del pedido", ex);
        }
    }
}
