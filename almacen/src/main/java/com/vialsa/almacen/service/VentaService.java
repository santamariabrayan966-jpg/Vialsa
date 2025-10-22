package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.VentaDao;
import com.vialsa.almacen.dao.VentaDetalleDao;
import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.model.Venta;
import com.vialsa.almacen.model.VentaDetalle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VentaService {

    private final VentaDao ventaDao;
    private final VentaDetalleDao detalleDao;
    private final ProductoService productoService;
    private final InventarioService inventarioService;

    public VentaService(VentaDao ventaDao,
                        VentaDetalleDao detalleDao,
                        ProductoService productoService,
                        InventarioService inventarioService) {
        this.ventaDao = ventaDao;
        this.detalleDao = detalleDao;
        this.productoService = productoService;
        this.inventarioService = inventarioService;
    }

    public Venta registrarVenta(Venta venta) {
        List<VentaDetalle> detalles = venta.getDetalles();
        if (detalles == null || detalles.isEmpty()) {
            throw new ServiceException("La venta debe contener al menos un producto");
        }
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        java.util.Map<Long, Producto> productosCache = new java.util.HashMap<>();
        for (VentaDetalle detalle : detalles) {
            Producto producto = productosCache.computeIfAbsent(detalle.getProductoId(), productoService::obtenerPorId);
            if (detalle.getCantidad() == null || detalle.getCantidad().signum() <= 0) {
                throw new ServiceException("La cantidad debe ser mayor a cero");
            }
            if (detalle.getPrecioUnitario() == null) {
                detalle.setPrecioUnitario(producto.getPrecio());
            }
            total = total.add(detalle.getSubtotal());
        }
        venta.setTotal(total);

        Venta creada = ventaDao.crear(venta);

        for (VentaDetalle detalle : detalles) {
            Producto producto = productosCache.get(detalle.getProductoId());
            detalle.setVentaId(creada.getId());
            inventarioService.registrarSalida(producto.getId(), detalle.getCantidad().intValue());
        }

        detalleDao.guardarDetalles(detalles);
        creada.setDetalles(detalles);
        creada.setTotal(total);
        return creada;
    }

    public List<Venta> listarVentas() {
        return ventaDao.findAll();
    }
}
