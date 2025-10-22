package com.vialsa.almacen.controller;

import com.vialsa.almacen.model.InventarioMovimiento;
import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.service.InventarioService;
import com.vialsa.almacen.service.ProductoService;

import java.util.List;

/**
 * Controlador que expone operaciones de inventario para las vistas.
 */
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductoService productoService;

    public InventarioController(InventarioService inventarioService, ProductoService productoService) {
        this.inventarioService = inventarioService;
        this.productoService = productoService;
    }

    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    public void registrarEntrada(Long productoId, int cantidad, String referencia) {
        inventarioService.registrarEntrada(productoId, cantidad, referencia);
    }

    public void registrarSalida(Long productoId, int cantidad, String referencia) {
        inventarioService.registrarSalida(productoId, cantidad, referencia);
    }

    public List<InventarioMovimiento> obtenerMovimientos(Long productoId) {
        return inventarioService.obtenerMovimientos(productoId);
    }
}
