package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.PedidoDao;
import com.vialsa.almacen.model.Pedido;
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
public class JdbcPedidoDao implements PedidoDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Pedido> mapper = (rs, rowNum) -> {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getLong("id"));
        pedido.setClienteId(rs.getLong("cliente_id"));
        Timestamp fecha = rs.getTimestamp("fecha");
        pedido.setFecha(fecha == null ? null : fecha.toLocalDateTime());
        pedido.setEstado(rs.getString("estado"));
        return pedido;
    };

    public JdbcPedidoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Pedido crear(Pedido pedido) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO pedidos (cliente_id, fecha, estado) VALUES (?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, pedido.getClienteId());
                LocalDateTime fecha = pedido.getFecha();
                ps.setTimestamp(2, fecha == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(fecha));
                ps.setString(3, pedido.getEstado());
                return ps;
            }, keyHolder);
            if (keyHolder.getKey() != null) {
                pedido.setId(keyHolder.getKey().longValue());
            }
            return pedido;
        } catch (DataAccessException ex) {
            throw new DaoException("Error al registrar pedido", ex);
        }
    }

    @Override
    public List<Pedido> findAll() {
        try {
            return jdbcTemplate.query("SELECT id, cliente_id, fecha, estado FROM pedidos ORDER BY fecha DESC", mapper);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar pedidos", ex);
        }
    }
}
