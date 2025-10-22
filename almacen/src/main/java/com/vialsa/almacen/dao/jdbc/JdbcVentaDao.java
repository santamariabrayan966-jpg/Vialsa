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
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class JdbcVentaDao implements VentaDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Venta> mapper = (rs, rowNum) -> {
        Venta venta = new Venta();
        venta.setId(rs.getLong("idVentas"));
        venta.setClienteId(rs.getLong("idClientes"));
        Timestamp fecha = rs.getTimestamp("FechaVenta");
        venta.setFecha(fecha == null ? null : fecha.toLocalDateTime());
        String clienteNombre = rs.getString("clienteNombre");
        venta.setClienteNombre(clienteNombre == null ? "" : clienteNombre.trim());
        java.math.BigDecimal total = rs.getBigDecimal("totalCalculado");
        venta.setTotal(total == null ? java.math.BigDecimal.ZERO : total);
        return venta;
    };

    public JdbcVentaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Venta crear(Venta venta) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            LocalDateTime fecha = venta.getFecha();
            if (fecha == null) {
                fecha = LocalDateTime.now();
                venta.setFecha(fecha);
            }
            LocalDateTime finalFecha = fecha;
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Ventas (FechaVenta, TipoComprobante, NroComprobante, idClientes, idUsuario) VALUES (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setTimestamp(1, Timestamp.valueOf(finalFecha));
                ps.setString(2, "BOLETA");
                ps.setString(3, UUID.randomUUID().toString());
                ps.setLong(4, venta.getClienteId());
                ps.setNull(5, Types.INTEGER);
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
            return jdbcTemplate.query(
                    "SELECT v.idVentas, v.idClientes, v.FechaVenta, " +
                            "TRIM(CONCAT(COALESCE(c.Nombres,''), ' ', COALESCE(c.Apellidos,''))) AS clienteNombre, " +
                            "COALESCE(SUM(dv.Cantidad * dv.PrecioUnitario), 0) AS totalCalculado " +
                            "FROM Ventas v " +
                            "LEFT JOIN Clientes c ON v.idClientes = c.idClientes " +
                            "LEFT JOIN DetalleVenta dv ON v.idVentas = dv.idVentas " +
                            "GROUP BY v.idVentas, v.idClientes, v.FechaVenta, clienteNombre " +
                            "ORDER BY v.FechaVenta DESC",
                    mapper);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar ventas", ex);
        }
    }
}
