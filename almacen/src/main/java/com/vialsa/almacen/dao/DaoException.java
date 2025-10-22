package com.vialsa.almacen.dao;

/**
 * Excepción checked para errores en operaciones con la base de datos.
 */
public class DaoException extends Exception {

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }
}
