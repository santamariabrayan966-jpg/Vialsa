package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.PedidoDetalle;

import java.util.List;

public interface PedidoDetalleDao {

    void guardarDetalles(List<PedidoDetalle> detalles) throws DaoException;

    List<PedidoDetalle> buscarPorPedido(Long pedidoId) throws DaoException;
}
