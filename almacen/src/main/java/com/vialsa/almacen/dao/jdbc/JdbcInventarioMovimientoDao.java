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
        movimiento.setId(rs.getLong("id"));
        movimiento.setProductoId(rs.getLong("producto_id"));
        movimiento.setTipo(InventarioMovimientoTipo.valueOf(rs.getString("tipo")));
        movimiento.setCantidad(rs.getInt("cantidad"));
        movimiento.setReferencia(rs.getString("referencia"));
        Timestamp timestamp = rs.getTimestamp("fecha");
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
                        "INSERT INTO inventario_movimientos (producto_id, tipo, cantidad, referencia, fecha) VALUES (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, movimiento.getProductoId());
                ps.setString(2, movimiento.getTipo().name());
                ps.setInt(3, movimiento.getCantidad());
                ps.setString(4, movimiento.getReferencia());
                LocalDateTime fecha = movimiento.getFecha();
                ps.setTimestamp(5, fecha == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(fecha));
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
                    "SELECT id, producto_id, tipo, cantidad, referencia, fecha FROM inventario_movimientos WHERE producto_id = ? ORDER BY fecha DESC",
                    mapper,
                    productoId);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar movimientos de inventario", ex);
        }
    }
}
