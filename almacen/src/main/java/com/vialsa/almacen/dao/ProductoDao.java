package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoDao {

    Producto guardar(Producto producto) throws DaoException;

    Optional<Producto> buscarPorId(Long id) throws DaoException;

    List<Producto> buscarTodos() throws DaoException;

    void actualizar(Producto producto) throws DaoException;

    void eliminar(Long id) throws DaoException;
}
