package com.vialsa.almacen.dao;

import com.vialsa.almacen.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoDao {
    List<Producto> findAll();

    Optional<Producto> findById(Long id);

    Producto save(Producto producto);

    void update(Producto producto);

    void updateStock(Long productoId, int nuevoStock);
}
