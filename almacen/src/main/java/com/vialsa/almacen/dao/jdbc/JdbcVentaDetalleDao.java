package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.VentaDetalleDao;
import com.vialsa.almacen.model.VentaDetalle;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcVentaDetalleDao implements VentaDetalleDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcVentaDetalleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void guardarDetalles(List<VentaDetalle> detalles) {
        try {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO venta_detalles (venta_id, producto_id, cantidad, precio_unitario) VALUES (?,?,?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            VentaDetalle detalle = detalles.get(i);
                            ps.setLong(1, detalle.getVentaId());
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
            throw new DaoException("Error al registrar detalles de la venta", ex);
        }
    }
}
