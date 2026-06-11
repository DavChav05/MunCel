package com.example.muncel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Dispositivo")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDispositivo;

    @Column(length = 50, nullable = false)
    private String marca;

    @Column(length = 50, nullable = false)
    private String modelo;

    @Column(name = "imei_o_serie", length = 20, nullable = false, unique = true)
    private String imeiOSeríe;

    // INTERCONEXIÓN RELACIONAL:
    @ManyToOne // "Muchos dispositivos pertenecen a Un Cliente"
    @JoinColumn(name = "id_cliente_fk", referencedColumnName = "idCliente",nullable = false) // Nombra la columna en MySQL
    private Cliente cliente;

    // --- CONSTRUCTORES, GETTERS Y SETTERS ---
    public Dispositivo() {
    }

    public Dispositivo(Integer idDispositivo, String marca, String modelo, String imeiOSeríe, Cliente cliente) {
        this.idDispositivo = idDispositivo;
        this.marca = marca;
        this.modelo = modelo;
        this.imeiOSeríe = imeiOSeríe;
        this.cliente = cliente;
    }

    public Integer getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(Integer idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getImeiOSeríe() {
        return imeiOSeríe;
    }

    public void setImeiOSeríe(String imeiOSeríe) {
        this.imeiOSeríe = imeiOSeríe;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    
}