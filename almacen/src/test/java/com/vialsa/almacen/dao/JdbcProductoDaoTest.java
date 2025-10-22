package com.vialsa.almacen.dao;

import com.vialsa.almacen.dao.jdbc.JdbcProductoDao;
import com.vialsa.almacen.model.Producto;
import com.vialsa.almacen.util.DatabaseManager;
import com.vialsa.almacen.util.TestDatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcProductoDaoTest {

    private JdbcProductoDao productoDao;

    @BeforeEach
    void setUp() {
        TestDatabaseHelper.initialize();
        productoDao = new JdbcProductoDao();
    }

    @Test
    void guardarYBuscarProducto() throws DaoException {
        Producto producto = new Producto("Laptop", "Ultrabook", new BigDecimal("1500.00"), 5);
        productoDao.guardar(producto);

        Optional<Producto> buscado = productoDao.buscarPorId(producto.getId());
        assertTrue(buscado.isPresent());
        assertEquals("Laptop", buscado.get().getNombre());
    }

    @Test
    void actualizarProducto() throws DaoException {
        Producto producto = new Producto("Mouse", "Óptico", new BigDecimal("20.00"), 10);
        productoDao.guardar(producto);

        producto.setDescripcion("Óptico inalámbrico");
        producto.setStock(15);
        productoDao.actualizar(producto);

        Producto actualizado = productoDao.buscarPorId(producto.getId()).orElseThrow();
        assertEquals("Óptico inalámbrico", actualizado.getDescripcion());
        assertEquals(15, actualizado.getStock());
    }

    @Test
    void eliminarProducto() throws DaoException, SQLException {
        Producto producto = new Producto("Teclado", "Mecánico", new BigDecimal("90.00"), 8);
        productoDao.guardar(producto);
        productoDao.eliminar(producto.getId());

        assertTrue(productoDao.buscarPorId(producto.getId()).isEmpty());
        assertEquals(0, contarProductos());
    }

    private int contarProductos() throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM productos");
             var resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        }
    }
}
