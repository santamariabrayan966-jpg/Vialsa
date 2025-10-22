package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoDao {

    Pedido guardar(Pedido pedido) throws DaoException;

    void actualizar(Pedido pedido) throws DaoException;

    Optional<Pedido> buscarPorId(Long id) throws DaoException;

    List<Pedido> buscarTodos() throws DaoException;
}
