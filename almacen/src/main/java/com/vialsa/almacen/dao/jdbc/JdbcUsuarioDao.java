package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.dao.UsuarioDao;
import com.vialsa.almacen.model.Usuario;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUsuarioDao implements UsuarioDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUsuarioDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        List<Usuario> usuarios = jdbcTemplate.query(
                "SELECT idUsuario AS id, TRIM(NombreUsuario) AS nombreUsuario, TRIM(Contrasena) AS contrasena, " +
                        "TRIM(Nombres) AS nombres, TRIM(Apellidos) AS apellidos, TRIM(Correo) AS correo, " +
                        "idRol AS idRol, idEstadoUsuario AS idEstadoUsuario " +
                        "FROM Usuarios WHERE LOWER(TRIM(NombreUsuario)) = LOWER(TRIM(?)) LIMIT 1",
                new BeanPropertyRowMapper<>(Usuario.class),
                nombreUsuario
        );

        if (usuarios.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(usuarios.get(0));
    }
}
