package com.vialsa.almacen.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto")
    private Integer idProducto;

    @Column(name = "CodigoInterno")
    private String codigoInterno;

    @Column(name = "NombreProducto")
    private String nombreProducto;

    @Column(name = "Dimensiones")
    private String dimensiones;

    @Column(name = "PrecioUnitario")
    private Double precioUnitario;

    @Column(name = "StockActual")
    private Integer stockActual;

    @Column(name = "StockMinimo")
    private Integer stockMinimo;

    @Column(name = "idUnidad")
    private Integer idUnidad;

    @Column(name = "idTipoProducto")
    private Integer idTipoProducto;

    @Column(name = "idEstadoProducto")
    private Integer idEstadoProducto;

    public Producto() {}

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getDimensiones() { return dimensiones; }
    public void setDimensiones(String dimensiones) { this.dimensiones = dimensiones; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public Integer getIdUnidad() { return idUnidad; }
    public void setIdUnidad(Integer idUnidad) { this.idUnidad = idUnidad; }

    public Integer getIdTipoProducto() { return idTipoProducto; }
    public void setIdTipoProducto(Integer idTipoProducto) { this.idTipoProducto = idTipoProducto; }

    public Integer getIdEstadoProducto() { return idEstadoProducto; }
    public void setIdEstadoProducto(Integer idEstadoProducto) { this.idEstadoProducto = idEstadoProducto; }
}
