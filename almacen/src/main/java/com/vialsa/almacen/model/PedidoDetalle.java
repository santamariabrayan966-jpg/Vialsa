package com.vialsa.almacen.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Detalle de un producto incluido en un pedido.
 */
public class PedidoDetalle {

    private Long id;
    private Long pedidoId;
    private Long productoId;
    private int cantidad;
    private BigDecimal precioUnitario;

    public PedidoDetalle() {
    }

    public PedidoDetalle(Long id, Long pedidoId, Long productoId, int cantidad, BigDecimal precioUnitario) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public PedidoDetalle(Long pedidoId, Long productoId, int cantidad, BigDecimal precioUnitario) {
        this(null, pedidoId, productoId, cantidad, precioUnitario);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
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
        if (!(o instanceof PedidoDetalle that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
