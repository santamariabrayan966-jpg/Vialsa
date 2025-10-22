package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.InventarioMovimientoDao;
import com.vialsa.almacen.dao.ProductoDao;
import com.vialsa.almacen.model.InventarioMovimiento;
import com.vialsa.almacen.model.InventarioMovimientoTipo;
import com.vialsa.almacen.model.Producto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InventarioService {

    private final InventarioMovimientoDao movimientoDao;
    private final ProductoDao productoDao;

    public InventarioService(InventarioMovimientoDao movimientoDao, ProductoDao productoDao) {
        this.movimientoDao = movimientoDao;
        this.productoDao = productoDao;
    }

    public List<Producto> obtenerInventario() {
        return productoDao.findAll();
    }

    public List<InventarioMovimiento> obtenerMovimientosPorProducto(Long productoId) {
        return movimientoDao.findByProducto(productoId);
    }

    public void registrarEntrada(Long productoId, int cantidad, String referencia) {
        ajustarInventario(productoId, cantidad, InventarioMovimientoTipo.ENTRADA, referencia);
    }

    public void registrarSalida(Long productoId, int cantidad, String referencia) {
        ajustarInventario(productoId, cantidad * -1, InventarioMovimientoTipo.SALIDA, referencia);
    }

    private void ajustarInventario(Long productoId, int delta, InventarioMovimientoTipo tipo, String referencia) {
        Producto producto = productoDao.findById(productoId)
                .orElseThrow(() -> new ServiceException("Producto no encontrado"));
        int nuevoStock = producto.getStock() + delta;
        if (nuevoStock < 0) {
            throw new ServiceException("No hay stock suficiente para completar la operación");
        }
        productoDao.updateStock(productoId, nuevoStock);

        InventarioMovimiento movimiento = new InventarioMovimiento();
        movimiento.setProductoId(productoId);
        movimiento.setCantidad(Math.abs(delta));
        movimiento.setTipo(tipo);
        movimiento.setReferencia(referencia);
        movimiento.setFecha(LocalDateTime.now());
        movimientoDao.registrarMovimiento(movimiento);
    }
}
