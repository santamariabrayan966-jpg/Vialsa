package com.vialsa.almacen.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registra los movimientos de inventario (entradas y salidas).
 */
public class InventarioMovimiento {

    public enum Tipo {
        ENTRADA,
        SALIDA
    }

    private Long id;
    private Long productoId;
    private Tipo tipo;
    private int cantidad;
    private LocalDateTime fechaMovimiento;
    private String referencia;

    public InventarioMovimiento() {
    }

    public InventarioMovimiento(Long id, Long productoId, Tipo tipo, int cantidad,
                                LocalDateTime fechaMovimiento, String referencia) {
        this.id = id;
        this.productoId = productoId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fechaMovimiento = fechaMovimiento;
        this.referencia = referencia;
    }

    public InventarioMovimiento(Long productoId, Tipo tipo, int cantidad,
                                LocalDateTime fechaMovimiento, String referencia) {
        this(null, productoId, tipo, cantidad, fechaMovimiento, referencia);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventarioMovimiento that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
