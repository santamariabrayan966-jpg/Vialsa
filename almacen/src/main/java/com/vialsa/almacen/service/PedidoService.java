package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.PedidoDao;
import com.vialsa.almacen.dao.PedidoDetalleDao;
import com.vialsa.almacen.model.Pedido;
import com.vialsa.almacen.model.PedidoDetalle;
import com.vialsa.almacen.model.Producto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PedidoService {

    private final PedidoDao pedidoDao;
    private final PedidoDetalleDao detalleDao;
    private final ProductoService productoService;
    private final InventarioService inventarioService;

    public PedidoService(PedidoDao pedidoDao,
                         PedidoDetalleDao detalleDao,
                         ProductoService productoService,
                         InventarioService inventarioService) {
        this.pedidoDao = pedidoDao;
        this.detalleDao = detalleDao;
        this.productoService = productoService;
        this.inventarioService = inventarioService;
    }

    public Pedido crearPedido(Pedido pedido) {
        List<PedidoDetalle> detalles = pedido.getDetalles();
        if (detalles == null || detalles.isEmpty()) {
            throw new ServiceException("El pedido debe contener al menos un producto");
        }
        pedido.setEstado("REGISTRADO");
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        java.util.Map<Long, Producto> productosCache = new java.util.HashMap<>();
        for (PedidoDetalle detalle : detalles) {
            Producto producto = productosCache.computeIfAbsent(detalle.getProductoId(), productoService::obtenerPorId);
            if (detalle.getCantidad() == null || detalle.getCantidad().signum() <= 0) {
                throw new ServiceException("La cantidad debe ser mayor a cero");
            }
            if (detalle.getPrecioUnitario() == null) {
                detalle.setPrecioUnitario(producto.getPrecio());
            }
            total = total.add(detalle.getSubtotal());
        }
        pedido.setTotal(total);

        Pedido creado = pedidoDao.crear(pedido);

        for (PedidoDetalle detalle : detalles) {
            Producto producto = productosCache.get(detalle.getProductoId());
            detalle.setPedidoId(creado.getId());
            inventarioService.registrarEntrada(producto.getId(), detalle.getCantidad().intValue());
        }

        detalleDao.guardarDetalles(detalles);
        creado.setDetalles(detalles);
        creado.setTotal(total);
        return creado;
    }

    public List<Pedido> listarPedidos() {
        return pedidoDao.findAll();
    }
}
