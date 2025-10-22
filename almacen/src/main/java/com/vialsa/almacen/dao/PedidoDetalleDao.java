package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.PedidoDetalle;

import java.util.List;

public interface PedidoDetalleDao {
    void guardarDetalles(List<PedidoDetalle> detalles);
}
