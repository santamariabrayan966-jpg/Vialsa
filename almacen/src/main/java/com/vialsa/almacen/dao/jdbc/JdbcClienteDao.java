package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.ClienteDao;
import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.model.Cliente;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcClienteDao implements ClienteDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Cliente> mapper = (rs, rowNum) -> {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("idClientes"));
        String nombres = rs.getString("nombres");
        String apellidos = rs.getString("apellidos");
        cliente.setNombre(String.format("%s %s", nombres == null ? "" : nombres.trim(), apellidos == null ? "" : apellidos.trim()).trim());
        cliente.setCorreo(rs.getString("correo"));
        cliente.setTelefono(rs.getString("telefono"));
        return cliente;
    };

    public JdbcClienteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Cliente> findAll() {
        try {
            return jdbcTemplate.query(
                    "SELECT idClientes, nombres, apellidos, correo, telefono FROM Clientes ORDER BY apellidos, nombres",
                    mapper);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar clientes", ex);
        }
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        try {
            List<Cliente> clientes = jdbcTemplate.query(
                    "SELECT idClientes, nombres, apellidos, correo, telefono FROM Clientes WHERE idClientes = ?",
                    mapper,
                    id);
            return clientes.stream().findFirst();
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar cliente", ex);
        }
    }
}
