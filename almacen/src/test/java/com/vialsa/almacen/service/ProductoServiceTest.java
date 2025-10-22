package com.vialsa.almacen.service;

import com.vialsa.almacen.dao.DaoException;
import com.vialsa.almacen.dao.jdbc.JdbcInventarioMovimientoDao;
import com.vialsa.almacen.dao.jdbc.JdbcProductoDao;
import com.vialsa.almacen.model.InventarioMovimiento;
import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.util.DatabaseManager;
import com.vialsa.almacen.util.TestDatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductoServiceTest {

    private JdbcProductoDao productoDao;
    private JdbcInventarioMovimientoDao inventarioMovimientoDao;
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        TestDatabaseHelper.initialize();
        productoDao = new JdbcProductoDao();
        inventarioMovimientoDao = new JdbcInventarioMovimientoDao();
        productoService = new ProductoService(productoDao, inventarioMovimientoDao);
    }

    @Test
    void registrarProductoGeneraMovimientoInicial() throws DaoException, SQLException {
        Producto producto = new Producto("Monitor", "24 pulgadas", new BigDecimal("250.00"), 4);
        productoService.registrarProducto(producto);

        assertNotNull(producto.getId());
        assertEquals(1, contarMovimientos(producto.getId()));
    }

    @Test
    void ajustarStockRegistraMovimiento() throws DaoException, SQLException {
        Producto producto = new Producto("Router", "WiFi 6", new BigDecimal("120.00"), 10);
        productoService.registrarProducto(producto);

        productoService.ajustarStock(producto, 3, InventarioMovimiento.Tipo.SALIDA, "Venta de prueba");

        Producto actualizado = productoDao.buscarPorId(producto.getId()).orElseThrow();
        assertEquals(7, actualizado.getStock());
        assertEquals(2, contarMovimientos(producto.getId()));
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
