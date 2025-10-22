package com.vialsa.almacen.service;

import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.model.Venta;
import com.vialsa.almacen.model.VentaDetalle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class VentaServiceTest {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @Test
    void registrarVentaDescuentaStock() {
        Producto producto = productoService.obtenerPorId(1L);
        int stockInicial = producto.getStock();

        Venta venta = new Venta();
        venta.setClienteId(1L);
        VentaDetalle detalle = new VentaDetalle();
        detalle.setProductoId(producto.getId());
        detalle.setCantidad(3);
        venta.setDetalles(List.of(detalle));

        ventaService.registrarVenta(venta);

        Producto actualizado = productoService.obtenerPorId(producto.getId());
        assertThat(actualizado.getStock()).isEqualTo(stockInicial - 3);
    }

    @Test
    void registrarVentaSinStockLanzaExcepcion() {
        Producto producto = productoService.obtenerPorId(2L);

        Venta venta = new Venta();
        venta.setClienteId(1L);
        VentaDetalle detalle = new VentaDetalle();
        detalle.setProductoId(producto.getId());
        detalle.setCantidad(producto.getStock() + 1);
        venta.setDetalles(List.of(detalle));

        assertThatThrownBy(() -> ventaService.registrarVenta(venta))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("stock");
    }
}
