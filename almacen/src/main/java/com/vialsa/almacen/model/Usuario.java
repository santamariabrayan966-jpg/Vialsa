package com.vialsa.almacen.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuarios")  // 👈 igual al nombre en tu BD
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")  // 👈 nombre exacto de la columna en la BD
    private Integer idUsuario;

    @Column(name = "NombreUsuario")
    private String nombreUsuario;

    @Column(name = "Contrasena")
    private String contrasena;

    // Getters y setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
