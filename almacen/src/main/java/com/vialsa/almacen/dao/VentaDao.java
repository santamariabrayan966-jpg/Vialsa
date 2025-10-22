package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.Venta;

import java.util.List;

public interface VentaDao {
    Venta crear(Venta venta);

    List<Venta> findAll();
}
