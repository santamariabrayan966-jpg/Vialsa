package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.VentaDetalle;

import java.util.List;

public interface VentaDetalleDao {

    void guardarDetalles(List<VentaDetalle> detalles) throws DaoException;

    List<VentaDetalle> buscarPorVenta(Long ventaId) throws DaoException;
}
