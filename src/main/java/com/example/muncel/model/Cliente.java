package com.example.muncel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Equivale a AUTO_INCREMENT
    private Integer idCliente;

    @Column(name = "cedula_ruc", length = 13, nullable = false, unique = true) // VARCHAR(13) NOT NULL UNIQUE
    private String cedulaRuc;

    @Column(name = "nombre_completo", length = 100, nullable = false) // VARCHAR(100) NOT NULL
    private String nombreCompleto;

    @Column(length = 15) // VARCHAR(15)
    private String telefono;

    @Column(length = 100) // VARCHAR(100)
    private String correo;

    // --- CONSTRUCTORES ---
    public Cliente() {
    }

    public Cliente(String cedulaRuc, String nombreCompleto, String telefono, String correo) {
        this.cedulaRuc = cedulaRuc;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setId(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getCedulaRuc() {
        return cedulaRuc;
    }

    public void setCedulaRuc(String cedulaRuc) {
        this.cedulaRuc = cedulaRuc;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}