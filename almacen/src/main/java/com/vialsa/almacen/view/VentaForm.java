package com.vialsa.almacen.view;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class VentaForm {

    @NotNull
    private Long clienteId;

    @NotNull
    private Long productoId;

    @Min(1)
    private int cantidad;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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
}
