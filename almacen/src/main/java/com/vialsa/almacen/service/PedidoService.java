package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.PedidoDao;
import com.vialsa.almacen.dao.PedidoDetalleDao;
import com.vialsa.almacen.dao.ProductoDao;
import com.vialsa.almacen.model.Pedido;
import com.vialsa.almacen.model.PedidoDetalle;
import com.vialsa.almacen.model.PedidoEstado;
import com.vialsa.almacen.model.Producto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio encargado de la orquestación de pedidos.
 */
public class PedidoService {

    private final PedidoDao pedidoDao;
    private final PedidoDetalleDao pedidoDetalleDao;
    private final ProductoDao productoDao;
    private final InventarioService inventarioService;

    public PedidoService(PedidoDao pedidoDao, PedidoDetalleDao pedidoDetalleDao,
                         ProductoDao productoDao, InventarioService inventarioService) {
        this.pedidoDao = pedidoDao;
        this.pedidoDetalleDao = pedidoDetalleDao;
        this.productoDao = productoDao;
        this.inventarioService = inventarioService;
    }

    public Pedido crearPedido(Long clienteId, List<PedidoDetalle> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un producto");
        }
        try {
            Pedido pedido = new Pedido(clienteId, LocalDateTime.now(), PedidoEstado.REGISTRADO);
            pedido = pedidoDao.guardar(pedido);
            for (PedidoDetalle detalle : detalles) {
                Producto producto = productoDao.buscarPorId(detalle.getProductoId())
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
                if (detalle.getPrecioUnitario() == null) {
                    detalle.setPrecioUnitario(producto.getPrecioUnitario());
                }
                inventarioService.registrarSalida(producto.getId(), detalle.getCantidad(),
                        "Pedido #" + pedido.getId());
                detalle.setPedidoId(pedido.getId());
            }
            pedidoDetalleDao.guardarDetalles(detalles);
            return pedido;
        } catch (DaoException e) {
            throw new ServiceException("No fue posible crear el pedido", e);
        }
    }

    public void actualizarEstado(Long pedidoId, PedidoEstado nuevoEstado) {
        try {
            Pedido pedido = pedidoDao.buscarPorId(pedidoId)
                    .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
            pedido.setEstado(nuevoEstado);
            pedidoDao.actualizar(pedido);
        } catch (DaoException e) {
            throw new ServiceException("No fue posible actualizar el estado del pedido", e);
        }
    }
}
