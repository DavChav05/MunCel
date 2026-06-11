package com.example.muncel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Proveedor")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Equivale a AUTO_INCREMENT
    private Integer idProveedor;

    @Column(name = "razon_social", length = 100, nullable = false) // VARCHAR(100) NOT NULL
    private String nombre;

    @Column(length = 15) // VARCHAR(15)
    private String telefono;

    @Column(length = 25) 
    private String sector;

    // --- CONSTRUCTORES ---
    
    // Constructor vacío
    public Proveedor() {
    }

    // Constructor lleno 
    public Proveedor(String nombre, String telefono, String sector) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.sector = sector;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    
}