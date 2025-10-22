package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.InventarioMovimientoDao;
import com.vialsa.almacen.model.InventarioMovimiento;
import com.vialsa.almacen.model.InventarioMovimientoTipo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcInventarioMovimientoDao implements InventarioMovimientoDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<InventarioMovimiento> mapper = (rs, rowNum) -> {
        InventarioMovimiento movimiento = new InventarioMovimiento();
        movimiento.setId(rs.getLong("idMovimientosAlmacen"));
        movimiento.setProductoId(rs.getLong("idProducto"));
        String tipo = rs.getString("TipoMovimiento");
        movimiento.setTipo(InventarioMovimientoTipo.valueOf(tipo == null ? "ENTRADA" : tipo.toUpperCase()));
        movimiento.setCantidad(rs.getBigDecimal("Cantidad").intValue());
        Timestamp timestamp = rs.getTimestamp("Fecha");
        movimiento.setFecha(timestamp == null ? null : timestamp.toLocalDateTime());
        return movimiento;
    };

    public JdbcInventarioMovimientoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void registrarMovimiento(InventarioMovimiento movimiento) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO MovimientosAlmacen (TipoMovimiento, Cantidad, Fecha, idProducto) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, movimiento.getTipo().name());
                ps.setBigDecimal(2, java.math.BigDecimal.valueOf(movimiento.getCantidad()));
                LocalDateTime fecha = movimiento.getFecha();
                ps.setTimestamp(3, fecha == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(fecha));
                ps.setLong(4, movimiento.getProductoId());
                return ps;
            }, keyHolder);
            if (keyHolder.getKey() != null) {
                movimiento.setId(keyHolder.getKey().longValue());
            }
        } catch (DataAccessException ex) {
            throw new DaoException("Error al registrar movimiento de inventario", ex);
        }
    }

    @Override
    public List<InventarioMovimiento> findByProducto(Long productoId) {
        try {
            return jdbcTemplate.query(
                    "SELECT idMovimientosAlmacen, idProducto, TipoMovimiento, Cantidad, Fecha FROM MovimientosAlmacen WHERE idProducto = ? ORDER BY Fecha DESC",
                    mapper,
                    productoId);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar movimientos de inventario", ex);
        }
    }
}
