package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.Usuario;

import java.util.Optional;

public interface UsuarioDao {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}

