package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.Pedido;

import java.util.List;

public interface PedidoDao {
    Pedido crear(Pedido pedido);

    List<Pedido> findAll();
}
