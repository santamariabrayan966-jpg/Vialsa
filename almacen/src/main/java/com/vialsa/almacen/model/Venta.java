package com.vialsa.almacen.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa una venta confirmada.
 */
public class Venta {

    private Long id;
    private Long clienteId;
    private LocalDateTime fechaVenta;
    private double total;

    public Venta() {
    }

    public Venta(Long id, Long clienteId, LocalDateTime fechaVenta, double total) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaVenta = fechaVenta;
        this.total = total;
    }

    public Venta(Long clienteId, LocalDateTime fechaVenta, double total) {
        this(null, clienteId, fechaVenta, total);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta venta)) {
            return false;
        }
        return Objects.equals(id, venta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
