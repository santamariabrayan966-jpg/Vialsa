package com.vialsa.almacen.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Detalle de cada producto vendido.
 */
public class VentaDetalle {

    private Long id;
    private Long ventaId;
    private Long productoId;
    private int cantidad;
    private BigDecimal precioUnitario;

    public VentaDetalle() {
    }

    public VentaDetalle(Long id, Long ventaId, Long productoId, int cantidad, BigDecimal precioUnitario) {
        this.id = id;
        this.ventaId = ventaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public VentaDetalle(Long ventaId, Long productoId, int cantidad, BigDecimal precioUnitario) {
        this(null, ventaId, productoId, cantidad, precioUnitario);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentaDetalle that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
