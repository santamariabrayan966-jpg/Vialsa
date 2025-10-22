package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.InventarioMovimientoDao;
import com.vialsa.almacen.dao.ProductoDao;
import com.vialsa.almacen.model.InventarioMovimiento;
import com.vialsa.almacen.model.Producto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de las operaciones de productos e inventario.
 */
public class ProductoService {

    private final ProductoDao productoDao;
    private final InventarioMovimientoDao inventarioMovimientoDao;

    public ProductoService(ProductoDao productoDao, InventarioMovimientoDao inventarioMovimientoDao) {
        this.productoDao = productoDao;
        this.inventarioMovimientoDao = inventarioMovimientoDao;
    }

    public Producto registrarProducto(Producto producto) {
        try {
            Producto creado = productoDao.guardar(producto);
            if (producto.getStock() > 0) {
                registrarMovimientoInicial(creado);
            }
            return creado;
        } catch (DaoException e) {
            throw new ServiceException("No fue posible registrar el producto", e);
        }
    }

    public Optional<Producto> obtenerProducto(Long id) {
        try {
            return productoDao.buscarPorId(id);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible obtener el producto", e);
        }
    }

    public List<Producto> listarProductos() {
        try {
            return productoDao.buscarTodos();
        } catch (DaoException e) {
            throw new ServiceException("No fue posible listar los productos", e);
        }
    }

    public void actualizarProducto(Producto producto) {
        try {
            productoDao.actualizar(producto);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible actualizar el producto", e);
        }
    }

    public void eliminarProducto(Long id) {
        try {
            productoDao.eliminar(id);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible eliminar el producto", e);
        }
    }

    public void ajustarStock(Producto producto, int cantidad, InventarioMovimiento.Tipo tipo, String referencia) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        try {
            int nuevoStock = tipo == InventarioMovimiento.Tipo.ENTRADA
                    ? producto.getStock() + cantidad
                    : producto.getStock() - cantidad;
            if (nuevoStock < 0) {
                throw new IllegalArgumentException("No hay stock suficiente para registrar la salida");
            }
            producto.setStock(nuevoStock);
            productoDao.actualizar(producto);
            InventarioMovimiento movimiento = new InventarioMovimiento(
                    producto.getId(),
                    tipo,
                    cantidad,
                    LocalDateTime.now(),
                    referencia
            );
            inventarioMovimientoDao.registrarMovimiento(movimiento);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible ajustar el stock del producto", e);
        }
    }

    private void registrarMovimientoInicial(Producto producto) throws DaoException {
        InventarioMovimiento movimiento = new InventarioMovimiento(
                producto.getId(),
                InventarioMovimiento.Tipo.ENTRADA,
                producto.getStock(),
                LocalDateTime.now(),
                "Carga inicial"
        );
        inventarioMovimientoDao.registrarMovimiento(movimiento);
    }
}
