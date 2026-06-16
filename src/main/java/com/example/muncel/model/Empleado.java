package com.example.muncel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpleado;

    @Column(name = "nombre_completo", length = 100, nullable = false)
    private String nombreCompleto;

    @Column(length = 15)
    private String telefono;

    // --- NUEVOS CAMPOS PARA INICIAR SESIÓN ---
    @Column(length = 50, nullable = false, unique = true)
    private String username; // Ej: "david.tecnico"

    @Column(length = 255, nullable = false)
    private String password; // Contraseña encriptada
    // -----------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolEmpleado rol; // ADMINISTRADOR, TECNICO, VENDEDOR

    @Column(name = "estado_activo", nullable = false)
    private Boolean estadoActivo = true;

    public Empleado() {
    }

    public Empleado(Integer idEmpleado, String nombreCompleto, String telefono, String username, String password,
            RolEmpleado rol, Boolean estadoActivo) {
        this.idEmpleado = idEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.estadoActivo = estadoActivo;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolEmpleado getRol() {
        return rol;
    }

    public void setRol(RolEmpleado rol) {
        this.rol = rol;
    }

    public boolean isEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

}