package com.vialsa.almacen.service;

/**
 * Excepción unchecked para comunicar errores en la capa de servicios.
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
