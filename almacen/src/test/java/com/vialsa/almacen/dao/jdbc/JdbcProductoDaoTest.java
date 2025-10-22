package com.vialsa.almacen.dao.jdbc;

import com.vialsa.almacen.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcProductoDao.class)
@ActiveProfiles("test")
class JdbcProductoDaoTest {

    @Autowired
    private JdbcProductoDao productoDao;

    @Test
    void listarProductosDevuelveRegistrosIniciales() {
        List<Producto> productos = productoDao.findAll();
        assertThat(productos).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void guardarProductoGeneraId() {
        Producto producto = new Producto();
        producto.setNombre("Broca SDS Max");
        producto.setDescripcion("Broca de 1" + '½' + " para concreto");
        producto.setPrecio(new BigDecimal("1200.00"));
        producto.setStock(10);

        Producto guardado = productoDao.save(producto);

        assertThat(guardado.getId()).isNotNull();
        assertThat(productoDao.findAll()).extracting(Producto::getNombre)
                .contains("Broca SDS Max");
    }

    @Test
    void actualizarStockModificaCantidad() {
        List<Producto> productos = productoDao.findAll();
        Producto producto = productos.getFirst();
        productoDao.updateStock(producto.getId(), 999);

        Producto actualizado = productoDao.findById(producto.getId()).orElseThrow();
        assertThat(actualizado.getStock()).isEqualTo(999);
    }
}
