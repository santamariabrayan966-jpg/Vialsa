package com.vialsa.almacen.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Pedido realizado por un cliente.
 */
public class Pedido {

    private Long id;
    private Long clienteId;
    private LocalDateTime fechaCreacion;
    private PedidoEstado estado;

    public Pedido() {
    }

    public Pedido(Long id, Long clienteId, LocalDateTime fechaCreacion, PedidoEstado estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Pedido(Long clienteId, LocalDateTime fechaCreacion, PedidoEstado estado) {
        this(null, clienteId, fechaCreacion, estado);
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public PedidoEstado getEstado() {
        return estado;
    }

    public void setEstado(PedidoEstado estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pedido pedido)) {
            return false;
        }
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
