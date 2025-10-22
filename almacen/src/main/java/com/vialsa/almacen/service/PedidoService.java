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
        Pedido creado = pedidoDao.crear(pedido);

        for (PedidoDetalle detalle : detalles) {
            Producto producto = productoService.obtenerPorId(detalle.getProductoId());
            if (detalle.getCantidad() <= 0) {
                throw new ServiceException("La cantidad debe ser mayor a cero");
            }
            detalle.setPedidoId(creado.getId());
            if (detalle.getPrecioUnitario() == null) {
                detalle.setPrecioUnitario(producto.getPrecio());
            }
            inventarioService.registrarEntrada(producto.getId(), detalle.getCantidad(), "Pedido " + creado.getId());
        }

        detalleDao.guardarDetalles(detalles);
        creado.setDetalles(detalles);
        return creado;
    }

    public List<Pedido> listarPedidos() {
        return pedidoDao.findAll();
    }
}
