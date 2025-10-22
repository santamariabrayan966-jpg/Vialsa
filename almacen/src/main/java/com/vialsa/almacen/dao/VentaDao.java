package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaDao {

    Venta guardar(Venta venta) throws DaoException;

    Optional<Venta> buscarPorId(Long id) throws DaoException;

    List<Venta> buscarTodos() throws DaoException;
}
