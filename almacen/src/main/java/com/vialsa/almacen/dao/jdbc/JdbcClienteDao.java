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
        cliente.setId(rs.getLong("id"));
        cliente.setNombre(rs.getString("nombre"));
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
            return jdbcTemplate.query("SELECT id, nombre, correo, telefono FROM clientes ORDER BY nombre", mapper);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar clientes", ex);
        }
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        try {
            List<Cliente> clientes = jdbcTemplate.query("SELECT id, nombre, correo, telefono FROM clientes WHERE id = ?", mapper, id);
            return clientes.stream().findFirst();
        } catch (DataAccessException ex) {
            throw new DaoException("Error al consultar cliente", ex);
        }
    }
}
