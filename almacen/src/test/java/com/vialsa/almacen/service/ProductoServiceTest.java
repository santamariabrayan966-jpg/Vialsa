package com.vialsa.almacen.service;

import com.vialsa.almacen.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @Test
    void registrarProductoIncrementaListado() {
        int inicial = productoService.listarProductos().size();

        Producto producto = new Producto();
        producto.setNombre("Cinta métrica 8m");
        producto.setDescripcion("Cuerpo metálico");
        producto.setPrecio(new BigDecimal("180.00"));
        producto.setStock(20);

        productoService.registrarProducto(producto);

        assertThat(productoService.listarProductos()).hasSize(inicial + 1);
    }

    @Test
    void registrarProductoConPrecioInvalidoLanzaExcepcion() {
        Producto producto = new Producto();
        producto.setNombre("Casco de seguridad");
        producto.setPrecio(BigDecimal.ZERO);

        assertThatThrownBy(() -> productoService.registrarProducto(producto))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("precio");
    }
}
