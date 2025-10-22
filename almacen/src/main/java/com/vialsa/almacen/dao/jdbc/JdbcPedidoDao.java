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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcPedidoDao implements PedidoDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Pedido> mapper = (rs, rowNum) -> {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getLong("idPedido"));
        pedido.setClienteId(rs.getLong("idCliente"));
        Timestamp fecha = rs.getTimestamp("FechaPedido");
        pedido.setFecha(fecha == null ? null : fecha.toLocalDateTime());
        String estado = rs.getString("NombreEstado");
        pedido.setEstado(estado == null ? "REGISTRADO" : estado);
        String clienteNombre = rs.getString("clienteNombre");
        pedido.setClienteNombre(clienteNombre == null ? "" : clienteNombre.trim());
        java.math.BigDecimal total = rs.getBigDecimal("TotalEstimado");
        pedido.setTotal(total == null ? java.math.BigDecimal.ZERO : total);
        return pedido;
    };

    public JdbcPedidoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Pedido crear(Pedido pedido) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            LocalDateTime fecha = pedido.getFecha();
            if (fecha == null) {
                fecha = LocalDateTime.now();
                pedido.setFecha(fecha);
            }
            LocalDateTime finalFecha = fecha;
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Pedidos (FechaPedido, DireccionEntrega, TotalEstimado, idCliente, idUsuario, idEstadoPedido) VALUES (?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setTimestamp(1, Timestamp.valueOf(finalFecha));
                ps.setString(2, "");
                BigDecimal total = pedido.getTotal() == null ? BigDecimal.ZERO : pedido.getTotal();
                ps.setBigDecimal(3, total);
                ps.setLong(4, pedido.getClienteId());
                ps.setNull(5, Types.INTEGER);
                ps.setNull(6, Types.INTEGER);
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
            return jdbcTemplate.query(
                    "SELECT p.idPedido, p.idCliente, p.FechaPedido, p.TotalEstimado, ep.NombreEstado, " +
                            "TRIM(CONCAT(COALESCE(c.Nombres,''), ' ', COALESCE(c.Apellidos,''))) AS clienteNombre " +
                            "FROM Pedidos p " +
                            "LEFT JOIN Clientes c ON p.idCliente = c.idClientes " +
                            "LEFT JOIN EstadoPedido ep ON p.idEstadoPedido = ep.idEstadoPedido " +
                            "ORDER BY p.FechaPedido DESC",
                    mapper);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar pedidos", ex);
        }
    }
}
