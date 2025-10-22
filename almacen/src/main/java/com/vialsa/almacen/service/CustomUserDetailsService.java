package com.vialsa.almacen.service;

import com.vialsa.almacen.model.Usuario;
import com.vialsa.almacen.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        System.out.println("Usuario encontrado: " + usuario);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario);
        }

        return User.builder()
                .username(usuario.getNombreUsuario())
                .password("{noop}" + usuario.getContrasena()) // 👈 importante si tu contraseña no está encriptada
                .roles("USER")
                .build();
    }
}
