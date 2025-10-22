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
        Venta creada = ventaDao.crear(venta);

        for (VentaDetalle detalle : detalles) {
            Producto producto = productoService.obtenerPorId(detalle.getProductoId());
            if (detalle.getCantidad() <= 0) {
                throw new ServiceException("La cantidad debe ser mayor a cero");
            }
            detalle.setVentaId(creada.getId());
            if (detalle.getPrecioUnitario() == null) {
                detalle.setPrecioUnitario(producto.getPrecio());
            }
            inventarioService.registrarSalida(producto.getId(), detalle.getCantidad(), "Venta " + creada.getId());
        }

        detalleDao.guardarDetalles(detalles);
        creada.setDetalles(detalles);
        return creada;
    }

    public List<Venta> listarVentas() {
        return ventaDao.findAll();
    }
}
