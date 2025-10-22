package com.vialsa.almacen.controller;

import com.vialsa.almacen.model.InventarioMovimientoTipo;
import com.vialsa.almacen.service.InventarioService;
import com.vialsa.almacen.view.InventarioAjusteForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/inventario")
    public String inventario(Model model) {
        model.addAttribute("inventario", inventarioService.obtenerInventario());
        model.addAttribute("movimientos", InventarioMovimientoTipo.values());
        model.addAttribute("ajusteForm", new InventarioAjusteForm());
        return "inventario/list";
    }

    @PostMapping("/inventario/ajustar")
    public String ajustarInventario(@Valid @ModelAttribute("ajusteForm") InventarioAjusteForm form,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("inventario", inventarioService.obtenerInventario());
            model.addAttribute("movimientos", InventarioMovimientoTipo.values());
            return "inventario/list";
        }

        if (InventarioMovimientoTipo.ENTRADA.name().equalsIgnoreCase(form.getTipo())) {
            inventarioService.registrarEntrada(form.getProductoId(), form.getCantidad());
        } else if (InventarioMovimientoTipo.SALIDA.name().equalsIgnoreCase(form.getTipo())) {
            inventarioService.registrarSalida(form.getProductoId(), form.getCantidad());
        } else {
            bindingResult.rejectValue("tipo", "tipo.invalido", "Tipo de movimiento no soportado");
            model.addAttribute("inventario", inventarioService.obtenerInventario());
            model.addAttribute("movimientos", InventarioMovimientoTipo.values());
            return "inventario/list";
        }
        return "redirect:/inventario";
    }
}
