package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.jdbc.JdbcClienteDao;
import com.vialsa.almacen.dao.jdbc.JdbcInventarioMovimientoDao;
import com.vialsa.almacen.dao.jdbc.JdbcProductoDao;
import com.vialsa.almacen.dao.jdbc.JdbcVentaDao;
import com.vialsa.almacen.dao.jdbc.JdbcVentaDetalleDao;
import com.vialsa.almacen.model.Cliente;
import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.model.Venta;
import com.vialsa.almacen.model.VentaDetalle;
import com.vialsa.almacen.util.DatabaseManager;
import com.vialsa.almacen.util.TestDatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VentaServiceTest {

    private JdbcClienteDao clienteDao;
    private JdbcProductoDao productoDao;
    private JdbcInventarioMovimientoDao inventarioMovimientoDao;
    private JdbcVentaDao ventaDao;
    private JdbcVentaDetalleDao ventaDetalleDao;
    private InventarioService inventarioService;
    private ProductoService productoService;
    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        TestDatabaseHelper.initialize();
        clienteDao = new JdbcClienteDao();
        productoDao = new JdbcProductoDao();
        inventarioMovimientoDao = new JdbcInventarioMovimientoDao();
        ventaDao = new JdbcVentaDao();
        ventaDetalleDao = new JdbcVentaDetalleDao();
        inventarioService = new InventarioService(productoDao, inventarioMovimientoDao);
        productoService = new ProductoService(productoDao, inventarioMovimientoDao);
        ventaService = new VentaService(ventaDao, ventaDetalleDao, productoDao, inventarioService);
    }

    @Test
    void registrarVentaDisminuyeStockYGeneraMovimiento() throws DaoException, SQLException {
        Cliente cliente = new Cliente("Juan", "Av. Siempre Viva", "555-1234", "juan@example.com");
        clienteDao.guardar(cliente);
        Producto producto = new Producto("Impresora", "Láser", new BigDecimal("350.00"), 5);
        productoService.registrarProducto(producto);

        VentaDetalle detalle = new VentaDetalle();
        detalle.setProductoId(producto.getId());
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(producto.getPrecioUnitario());
        Venta venta = ventaService.registrarVenta(cliente.getId(), Collections.singletonList(detalle));

        assertNotNull(venta.getId());
        Producto actualizado = productoDao.buscarPorId(producto.getId()).orElseThrow();
        assertEquals(3, actualizado.getStock());
        assertEquals(2, contarMovimientos(producto.getId()));
        assertEquals(700.0, venta.getTotal(), 0.001);
    }

    private int contarMovimientos(Long productoId) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM inventario_movimientos WHERE producto_id = ?")) {
            statement.setLong(1, productoId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}
