package com.vialsa.almacen.controller;

import com.vialsa.almacen.model.Cliente;
import com.vialsa.almacen.model.Pedido;
import com.vialsa.almacen.model.PedidoDetalle;
import com.vialsa.almacen.model.PedidoEstado;
import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.service.ClienteService;
import com.vialsa.almacen.service.PedidoService;
import com.vialsa.almacen.service.ProductoService;

import java.util.List;

/**
 * Controlador del flujo de pedidos.
 */
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    public PedidoController(PedidoService pedidoService, ClienteService clienteService,
                            ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    public Pedido crearPedido(Long clienteId, List<PedidoDetalle> detalles) {
        return pedidoService.crearPedido(clienteId, detalles);
    }

    public void actualizarEstado(Long pedidoId, PedidoEstado estado) {
        pedidoService.actualizarEstado(pedidoId, estado);
    }
}
