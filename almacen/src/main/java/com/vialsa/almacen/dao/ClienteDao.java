package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteDao {

    Cliente guardar(Cliente cliente) throws DaoException;

    Optional<Cliente> buscarPorId(Long id) throws DaoException;

    List<Cliente> buscarTodos() throws DaoException;

    void actualizar(Cliente cliente) throws DaoException;

    void eliminar(Long id) throws DaoException;
}
