package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.VentaDao;
import com.vialsa.almacen.model.Venta;
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
public class JdbcVentaDao implements VentaDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Venta> mapper = (rs, rowNum) -> {
        Venta venta = new Venta();
        venta.setId(rs.getLong("id"));
        venta.setClienteId(rs.getLong("cliente_id"));
        Timestamp fecha = rs.getTimestamp("fecha");
        venta.setFecha(fecha == null ? null : fecha.toLocalDateTime());
        return venta;
    };

    public JdbcVentaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Venta crear(Venta venta) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO ventas (cliente_id, fecha) VALUES (?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, venta.getClienteId());
                LocalDateTime fecha = venta.getFecha();
                ps.setTimestamp(2, fecha == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(fecha));
                return ps;
            }, keyHolder);
            if (keyHolder.getKey() != null) {
                venta.setId(keyHolder.getKey().longValue());
            }
            return venta;
        } catch (DataAccessException ex) {
            throw new DaoException("Error al registrar venta", ex);
        }
    }

    @Override
    public List<Venta> findAll() {
        try {
            return jdbcTemplate.query("SELECT id, cliente_id, fecha FROM ventas ORDER BY fecha DESC", mapper);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar ventas", ex);
        }
    }
}
