package com.vialsa.almacen.util;

import com.vialsa.almacen.util.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utilidad para inicializar una base de datos H2 en memoria durante las pruebas.
 */
public final class TestDatabaseHelper {

    private static boolean initialized = false;

    private TestDatabaseHelper() {
    }

    public static void initialize() {
        if (!initialized) {
            DatabaseManager.configureDefault("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "");
            crearEsquema();
            initialized = true;
        }
        limpiarDatos();
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

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS inventario_movimientos (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "producto_id BIGINT NOT NULL, " +
                    "tipo VARCHAR(20) NOT NULL, " +
                    "cantidad INT NOT NULL, " +
                    "fecha_movimiento TIMESTAMP NOT NULL, " +
                    "referencia VARCHAR(200))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS pedidos (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "cliente_id BIGINT NOT NULL, " +
                    "fecha_creacion TIMESTAMP NOT NULL, " +
                    "estado VARCHAR(30) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS pedido_detalles (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "pedido_id BIGINT NOT NULL, " +
                    "producto_id BIGINT NOT NULL, " +
                    "cantidad INT NOT NULL, " +
                    "precio_unitario DECIMAL(15,2) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ventas (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "cliente_id BIGINT NOT NULL, " +
                    "fecha_venta TIMESTAMP NOT NULL, " +
                    "total DECIMAL(15,2) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS venta_detalles (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "venta_id BIGINT NOT NULL, " +
                    "producto_id BIGINT NOT NULL, " +
                    "cantidad INT NOT NULL, " +
                    "precio_unitario DECIMAL(15,2) NOT NULL)");
        } catch (SQLException e) {
            throw new IllegalStateException("No fue posible preparar la base de datos de prueba", e);
        }
    }

    private static void limpiarDatos() {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM venta_detalles");
            statement.executeUpdate("DELETE FROM ventas");
            statement.executeUpdate("DELETE FROM pedido_detalles");
            statement.executeUpdate("DELETE FROM pedidos");
            statement.executeUpdate("DELETE FROM inventario_movimientos");
            statement.executeUpdate("DELETE FROM productos");
            statement.executeUpdate("DELETE FROM clientes");
        } catch (SQLException e) {
            throw new IllegalStateException("No fue posible limpiar la base de datos de prueba", e);
        }
    }
}
