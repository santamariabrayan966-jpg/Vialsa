package com.vialsa.almacen;

import com.vialsa.almacen.controller.InventarioController;
import com.vialsa.almacen.controller.PedidoController;
import com.vialsa.almacen.controller.VentaController;
import com.vialsa.almacen.dao.jdbc.JdbcClienteDao;
import com.vialsa.almacen.dao.jdbc.JdbcInventarioMovimientoDao;
import com.vialsa.almacen.dao.jdbc.JdbcPedidoDao;
import com.vialsa.almacen.dao.jdbc.JdbcPedidoDetalleDao;
import com.vialsa.almacen.dao.jdbc.JdbcProductoDao;
import com.vialsa.almacen.dao.jdbc.JdbcVentaDao;
import com.vialsa.almacen.dao.jdbc.JdbcVentaDetalleDao;
import com.vialsa.almacen.service.ClienteService;
import com.vialsa.almacen.service.InventarioService;
import com.vialsa.almacen.service.PedidoService;
import com.vialsa.almacen.service.ProductoService;
import com.vialsa.almacen.service.VentaService;
import com.vialsa.almacen.util.DatabaseManager;
import com.vialsa.almacen.view.InventoryView;
import com.vialsa.almacen.view.OrderView;
import com.vialsa.almacen.view.SalesView;

import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Punto de entrada de la aplicación de almacén.
 */
public final class AlmacenApplication {

    private AlmacenApplication() {
    }

    public static void main(String[] args) {
        DatabaseManager.configureDefault("jdbc:h2:mem:vialsa;DB_CLOSE_DELAY=-1", "sa", "");
        crearEsquema();

        JdbcClienteDao clienteDao = new JdbcClienteDao();
        JdbcProductoDao productoDao = new JdbcProductoDao();
        JdbcInventarioMovimientoDao inventarioMovimientoDao = new JdbcInventarioMovimientoDao();
        JdbcPedidoDao pedidoDao = new JdbcPedidoDao();
        JdbcPedidoDetalleDao pedidoDetalleDao = new JdbcPedidoDetalleDao();
        JdbcVentaDao ventaDao = new JdbcVentaDao();
        JdbcVentaDetalleDao ventaDetalleDao = new JdbcVentaDetalleDao();

        ClienteService clienteService = new ClienteService(clienteDao);
        InventarioService inventarioService = new InventarioService(productoDao, inventarioMovimientoDao);
        ProductoService productoService = new ProductoService(productoDao, inventarioMovimientoDao);
        PedidoService pedidoService = new PedidoService(pedidoDao, pedidoDetalleDao, productoDao, inventarioService);
        VentaService ventaService = new VentaService(ventaDao, ventaDetalleDao, productoDao, inventarioService);

        InventarioController inventarioController = new InventarioController(inventarioService, productoService);
        PedidoController pedidoController = new PedidoController(pedidoService, clienteService, productoService);
        VentaController ventaController = new VentaController(ventaService, clienteService, productoService);

        SwingUtilities.invokeLater(() -> {
            new InventoryView(inventarioController).setVisible(true);
            new OrderView(pedidoController).setVisible(true);
            new SalesView(ventaController).setVisible(true);
        });
    }

    private static void crearEsquema() {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS clientes (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL, " +
                    "direccion VARCHAR(200), " +
                    "telefono VARCHAR(30), " +
                    "email VARCHAR(120))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS productos (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "nombre VARCHAR(120) NOT NULL, " +
                    "descripcion VARCHAR(255), " +
                    "precio_unitario DECIMAL(15,2) NOT NULL, " +
                    "stock INT NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS pedidos (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "cliente_id BIGINT NOT NULL, " +
                    "fecha_creacion TIMESTAMP NOT NULL, " +
                    "estado VARCHAR(30) NOT NULL, " +
                    "FOREIGN KEY (cliente_id) REFERENCES clientes(id))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS pedido_detalles (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "pedido_id BIGINT NOT NULL, " +
                    "producto_id BIGINT NOT NULL, " +
                    "cantidad INT NOT NULL, " +
                    "precio_unitario DECIMAL(15,2) NOT NULL, " +
                    "FOREIGN KEY (pedido_id) REFERENCES pedidos(id), " +
                    "FOREIGN KEY (producto_id) REFERENCES productos(id))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ventas (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "cliente_id BIGINT NOT NULL, " +
                    "fecha_venta TIMESTAMP NOT NULL, " +
                    "total DECIMAL(15,2) NOT NULL, " +
                    "FOREIGN KEY (cliente_id) REFERENCES clientes(id))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS venta_detalles (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "venta_id BIGINT NOT NULL, " +
                    "producto_id BIGINT NOT NULL, " +
                    "cantidad INT NOT NULL, " +
                    "precio_unitario DECIMAL(15,2) NOT NULL, " +
                    "FOREIGN KEY (venta_id) REFERENCES ventas(id), " +
                    "FOREIGN KEY (producto_id) REFERENCES productos(id))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS inventario_movimientos (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "producto_id BIGINT NOT NULL, " +
                    "tipo VARCHAR(20) NOT NULL, " +
                    "cantidad INT NOT NULL, " +
                    "fecha_movimiento TIMESTAMP NOT NULL, " +
                    "referencia VARCHAR(200), " +
                    "FOREIGN KEY (producto_id) REFERENCES productos(id))");
        } catch (SQLException e) {
            throw new IllegalStateException("No fue posible inicializar la base de datos", e);
        }
    }
}
