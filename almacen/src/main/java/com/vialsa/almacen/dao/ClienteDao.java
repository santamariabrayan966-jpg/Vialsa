package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteDao {
    List<Cliente> findAll();

    Optional<Cliente> findById(Long id);
}
