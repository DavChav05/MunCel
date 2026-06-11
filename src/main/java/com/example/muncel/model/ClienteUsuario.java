package com.example.muncel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UsuarioCliente")
public class ClienteUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCU;

    @Column(length = 50, nullable = false, unique = true) // El nombre de usuario o email para loguearse
    private String username;

    @Column(length = 255, nullable = false) // 255 caracteres porque las contraseñas se guardan ENCRIPTADAS (Ej: con BCrypt)
    private String password;

    @OneToOne
    @JoinColumn(name = "id_cliente_fk", referencedColumnName = "idCliente", nullable = false, unique = true)
    private Cliente cliente; // Vinculación directa 1:1 con tus datos de cliente

    // --- CONSTRUCTORES ---
    public ClienteUsuario() {
    }

    public ClienteUsuario(String username, String password, Cliente cliente) {
        this.username = username;
        this.password = password;
        this.cliente = cliente;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getIdCU() {
        return idCU;
    }

    public void setIdCU(Integer idCU) {
        this.idCU = idCU;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}