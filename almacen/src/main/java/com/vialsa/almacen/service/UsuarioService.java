package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.UsuarioDao;
import com.vialsa.almacen.model.Usuario;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioDao usuarioDao;

    public UsuarioService(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        return usuarioDao.findByNombreUsuario(nombreUsuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.trim().isEmpty()) {
            throw new UsernameNotFoundException("El nombre de usuario no puede estar vacío");
        }

        Usuario usuario = usuarioDao.findByNombreUsuario(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró el usuario: " + username));
        if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
            throw new UsernameNotFoundException("El usuario no tiene una contraseña registrada");
        }

        if (usuario.getIdEstadoUsuario() != null && usuario.getIdEstadoUsuario() != 1) {
            throw new DisabledException("El usuario no se encuentra activo");
        }

        String rol = (usuario.getIdRol() != null && usuario.getIdRol() == 1)
                ? "ROLE_ADMIN"
                : "ROLE_USER";

        return new User(
                usuario.getNombreUsuario().trim(),
                usuario.getContrasena().trim(),
                Collections.singletonList(new SimpleGrantedAuthority(rol))
        );
    }
}
