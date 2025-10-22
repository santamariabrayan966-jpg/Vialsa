package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.InventarioMovimiento;

import java.util.List;

public interface InventarioMovimientoDao {
    void registrarMovimiento(InventarioMovimiento movimiento);

    List<InventarioMovimiento> findByProducto(Long productoId);
}
