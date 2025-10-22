package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.InventarioMovimiento;

import java.util.List;

public interface InventarioMovimientoDao {

    InventarioMovimiento registrarMovimiento(InventarioMovimiento movimiento) throws DaoException;

    List<InventarioMovimiento> buscarPorProducto(Long productoId) throws DaoException;
}
