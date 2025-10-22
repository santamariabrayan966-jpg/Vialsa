package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.ProductoDao;
import com.vialsa.almacen.dao.VentaDao;
import com.vialsa.almacen.dao.VentaDetalleDao;
import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.model.Venta;
import com.vialsa.almacen.model.VentaDetalle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio responsable de la gestión de ventas.
 */
public class VentaService {

    private final VentaDao ventaDao;
    private final VentaDetalleDao ventaDetalleDao;
    private final ProductoDao productoDao;
    private final InventarioService inventarioService;

    public VentaService(VentaDao ventaDao, VentaDetalleDao ventaDetalleDao,
                        ProductoDao productoDao, InventarioService inventarioService) {
        this.ventaDao = ventaDao;
        this.ventaDetalleDao = ventaDetalleDao;
        this.productoDao = productoDao;
        this.inventarioService = inventarioService;
    }

    public Venta registrarVenta(Long clienteId, List<VentaDetalle> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La venta debe contener al menos un producto");
        }
        try {
            double total = 0;
            for (VentaDetalle detalle : detalles) {
                Producto producto = productoDao.buscarPorId(detalle.getProductoId())
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
                if (detalle.getPrecioUnitario() == null) {
                    detalle.setPrecioUnitario(producto.getPrecioUnitario());
                }
                BigDecimal precio = detalle.getPrecioUnitario();
                total += precio.doubleValue() * detalle.getCantidad();
            }
            Venta venta = new Venta(clienteId, LocalDateTime.now(), total);
            venta = ventaDao.guardar(venta);
            for (VentaDetalle detalle : detalles) {
                detalle.setVentaId(venta.getId());
                inventarioService.registrarSalida(detalle.getProductoId(), detalle.getCantidad(),
                        "Venta #" + venta.getId());
            }
            ventaDetalleDao.guardarDetalles(detalles);
            return venta;
        } catch (DaoException e) {
            throw new ServiceException("No fue posible registrar la venta", e);
        }
    }
}
