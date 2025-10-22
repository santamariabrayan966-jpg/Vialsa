package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.InventarioMovimientoDao;
import com.vialsa.almacen.dao.ProductoDao;
import com.vialsa.almacen.model.InventarioMovimiento;
import com.vialsa.almacen.model.Producto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio que encapsula las reglas de negocio de inventario.
 */
public class InventarioService {

    private final ProductoDao productoDao;
    private final InventarioMovimientoDao inventarioMovimientoDao;

    public InventarioService(ProductoDao productoDao, InventarioMovimientoDao inventarioMovimientoDao) {
        this.productoDao = productoDao;
        this.inventarioMovimientoDao = inventarioMovimientoDao;
    }

    public void registrarEntrada(Long productoId, int cantidad, String referencia) {
        registrarMovimiento(productoId, cantidad, referencia, InventarioMovimiento.Tipo.ENTRADA);
    }

    public void registrarSalida(Long productoId, int cantidad, String referencia) {
        registrarMovimiento(productoId, cantidad, referencia, InventarioMovimiento.Tipo.SALIDA);
    }

    public List<InventarioMovimiento> obtenerMovimientos(Long productoId) {
        try {
            return inventarioMovimientoDao.buscarPorProducto(productoId);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible consultar los movimientos de inventario", e);
        }
    }

    private void registrarMovimiento(Long productoId, int cantidad, String referencia, InventarioMovimiento.Tipo tipo) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        try {
            Producto producto = productoDao.buscarPorId(productoId)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            int nuevoStock = tipo == InventarioMovimiento.Tipo.ENTRADA
                    ? producto.getStock() + cantidad
                    : producto.getStock() - cantidad;
            if (nuevoStock < 0) {
                throw new IllegalArgumentException("No hay stock suficiente");
            }
            producto.setStock(nuevoStock);
            productoDao.actualizar(producto);
            InventarioMovimiento movimiento = new InventarioMovimiento(
                    productoId,
                    tipo,
                    cantidad,
                    LocalDateTime.now(),
                    referencia
            );
            inventarioMovimientoDao.registrarMovimiento(movimiento);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible registrar el movimiento de inventario", e);
        }
    }
}
