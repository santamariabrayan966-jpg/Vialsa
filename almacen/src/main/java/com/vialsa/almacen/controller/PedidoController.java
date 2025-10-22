package com.vialsa.almacen.controller;

import com.vialsa.almacen.model.Pedido;
import com.vialsa.almacen.model.PedidoDetalle;
import com.vialsa.almacen.service.ClienteService;
import com.vialsa.almacen.service.PedidoService;
import com.vialsa.almacen.service.ProductoService;
import com.vialsa.almacen.view.PedidoForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    public PedidoController(PedidoService pedidoService,
                            ClienteService clienteService,
                            ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    @GetMapping("/pedidos")
    public String pedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarPedidos());
        model.addAttribute("clientes", clienteService.obtenerClientes());
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("pedidoForm", new PedidoForm());
        return "pedidos/list";
    }

    @PostMapping("/pedidos")
    public String crearPedido(@Valid @ModelAttribute("pedidoForm") PedidoForm form,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pedidos", pedidoService.listarPedidos());
            model.addAttribute("clientes", clienteService.obtenerClientes());
            model.addAttribute("productos", productoService.listarProductos());
            return "pedidos/list";
        }

        Pedido pedido = new Pedido();
        pedido.setClienteId(form.getClienteId());
        PedidoDetalle detalle = new PedidoDetalle();
        detalle.setProductoId(form.getProductoId());
        detalle.setCantidad(java.math.BigDecimal.valueOf(form.getCantidad()));
        pedido.setDetalles(List.of(detalle));
        pedidoService.crearPedido(pedido);
        return "redirect:/pedidos";
    }
}
