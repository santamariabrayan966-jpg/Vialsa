package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.ProductoDao;
import com.vialsa.almacen.model.Producto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoDao productoDao;

    public ProductoService(ProductoDao productoDao) {
        this.productoDao = productoDao;
    }

    public List<Producto> listarProductos() {
        return productoDao.findAll();
    }

    public Producto registrarProducto(Producto producto) {
        validarProducto(producto);
        if (producto.getStock() < 0) {
            producto.setStock(0);
        }
        return productoDao.save(producto);
    }

    public void actualizarProducto(Producto producto) {
        validarProducto(producto);
        productoDao.update(producto);
    }

    public Producto obtenerPorId(Long id) {
        return productoDao.findById(id)
                .orElseThrow(() -> new ServiceException("Producto no encontrado"));
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new ServiceException("El nombre del producto es obligatorio");
        }
        BigDecimal precio = producto.getPrecio();
        if (precio == null || precio.signum() <= 0) {
            throw new ServiceException("El precio debe ser mayor a cero");
        }
    }
}
