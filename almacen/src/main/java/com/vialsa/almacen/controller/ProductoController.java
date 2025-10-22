package com.vialsa.almacen.controller;

import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.service.ProductoService;
import com.vialsa.almacen.view.ProductoForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("productoForm", new ProductoForm());
        return "productos/list";
    }

    @PostMapping("/productos")
    public String crearProducto(@Valid @ModelAttribute("productoForm") ProductoForm form,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productos", productoService.listarProductos());
            return "productos/list";
        }
        Producto producto = new Producto();
        producto.setNombre(form.getNombre());
        producto.setDescripcion(form.getDescripcion());
        producto.setPrecio(form.getPrecio());
        producto.setStock(form.getStock());
        productoService.registrarProducto(producto);
        return "redirect:/productos";
    }
}
