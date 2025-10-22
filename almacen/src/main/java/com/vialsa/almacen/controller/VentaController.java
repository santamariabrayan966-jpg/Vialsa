package com.vialsa.almacen.controller;

import com.vialsa.almacen.model.Cliente;
import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.model.Venta;
import com.vialsa.almacen.model.VentaDetalle;
import com.vialsa.almacen.service.ClienteService;
import com.vialsa.almacen.service.ProductoService;
import com.vialsa.almacen.service.VentaService;

import java.util.List;

/**
 * Controlador que expone el flujo de ventas.
 */
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    public VentaController(VentaService ventaService, ClienteService clienteService,
                           ProductoService productoService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    public Venta registrarVenta(Long clienteId, List<VentaDetalle> detalles) {
        return ventaService.registrarVenta(clienteId, detalles);
    }
}
