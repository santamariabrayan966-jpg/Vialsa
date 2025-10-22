package com.vialsa.almacen.controller;

import com.vialsa.almacen.model.Venta;
import com.vialsa.almacen.model.VentaDetalle;
import com.vialsa.almacen.service.ClienteService;
import com.vialsa.almacen.service.ProductoService;
import com.vialsa.almacen.service.VentaService;
import com.vialsa.almacen.view.VentaForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    public VentaController(VentaService ventaService,
                           ClienteService clienteService,
                           ProductoService productoService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    @GetMapping("/ventas")
    public String ventas(Model model) {
        model.addAttribute("ventas", ventaService.listarVentas());
        model.addAttribute("clientes", clienteService.obtenerClientes());
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("ventaForm", new VentaForm());
        return "ventas/list";
    }

    @PostMapping("/ventas")
    public String registrarVenta(@Valid @ModelAttribute("ventaForm") VentaForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ventas", ventaService.listarVentas());
            model.addAttribute("clientes", clienteService.obtenerClientes());
            model.addAttribute("productos", productoService.listarProductos());
            return "ventas/list";
        }

        Venta venta = new Venta();
        venta.setClienteId(form.getClienteId());
        VentaDetalle detalle = new VentaDetalle();
        detalle.setProductoId(form.getProductoId());
        detalle.setCantidad(java.math.BigDecimal.valueOf(form.getCantidad()));
        venta.setDetalles(List.of(detalle));
        ventaService.registrarVenta(venta);
        return "redirect:/ventas";
    }
}
