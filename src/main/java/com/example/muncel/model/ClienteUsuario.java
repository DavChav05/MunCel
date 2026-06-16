package com.example.muncel.model;

import java.util.Base64;
import java.security.MessageDigest;
import jakarta.persistence.*;

@Entity
@Table(name = "UsuarioCliente")
public class ClienteUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCU;

    @Column(length = 100)
    private String correo;

    @Column(length = 50, nullable = false, unique = true) // El nombre de usuario o email para loguearse
    private String username;

    @Column(length = 255, nullable = false) // 255 caracteres porque las contraseñas se guardan ENCRIPTADAS (Ej: con
                                            // BCrypt)
    private String password;

    @OneToOne
    @JoinColumn(name = "id_cliente_fk", referencedColumnName = "idCliente", nullable = false, unique = true)
    private Cliente cliente; // Vinculación directa 1:1 con tus datos de cliente

    // --- CONSTRUCTORES ---
    public ClienteUsuario() {
    }

    public ClienteUsuario(String username, String password, String correo, Cliente cliente) {
        this.username = username;
        this.password = password;
        this.cliente = cliente;
        this.correo = correo;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    // MAGIA DE ENCRIPTACIÓN EN EL ATRIBUTO
    public void setPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            // Se guarda la contraseña como un texto encriptado ilegible
            this.password = Base64.getEncoder().encodeToString(hash);
        } catch (Exception ex) {
            throw new RuntimeException("Error encriptando la contraseña", ex);
        }
    }

    // MÉTODO AUXILIAR PARA EL LOGIN
    public boolean verificarPasswordIngresada(String passwordIngresada) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(passwordIngresada.getBytes("UTF-8"));
            String hashIngresado = Base64.getEncoder().encodeToString(hash);
            return this.password.equals(hashIngresado);
        } catch (Exception ex) {
            return false;
        }
    }
}