package com.vialsa.almacen.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Gestiona la configuración de conexiones JDBC para toda la aplicación.
 */
public final class DatabaseManager {

    private static Supplier<Connection> connectionSupplier;

    private DatabaseManager() {
    }

    /**
     * Configura la forma en la que se obtendrán las conexiones JDBC.
     *
     * @param supplier proveedor de conexiones
     */
    public static void setConnectionSupplier(Supplier<Connection> supplier) {
        connectionSupplier = supplier;
    }

    /**
     * Registra una conexión JDBC simple basada en {@link DriverManager}.
     */
    public static void configureDefault(String url, String username, String password) {
        setConnectionSupplier(() -> {
            try {
                return DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new IllegalStateException("No fue posible obtener la conexión a la base de datos", e);
            }
        });
    }

    /**
     * Obtiene una nueva conexión utilizando el proveedor configurado.
     */
    public static Connection getConnection() {
        if (Objects.isNull(connectionSupplier)) {
            throw new IllegalStateException("No se ha configurado un proveedor de conexiones");
        }
        return connectionSupplier.get();
    }
}
