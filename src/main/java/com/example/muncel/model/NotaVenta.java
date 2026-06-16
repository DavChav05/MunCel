package com.example.muncel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;

@Entity
@Table(name = "NotaVenta")
public class NotaVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Equivale a AUTO_INCREMENT
    private Integer idNotaVenta;

    @Column(name = "numero_factura", nullable = false, unique = true)
    private Integer numeroFactura;

    @Column(name = "fecha_emision", nullable = false) // DATETIME DEFAULT CURRENT_TIMESTAMP
    private LocalDateTime fechaEmision = LocalDateTime.now(); // Asigna la fecha y hora actual automáticamente

    // Usamos BigDecimal para evitar pérdidas de centavos en el total
    @Column(name = "total_pagar", nullable = false, precision = 10, scale = 2) // DECIMAL(10,2) NOT NULL DEFAULT 0.00
    private BigDecimal totalPagar = BigDecimal.ZERO;

    // RELACIÓN: Muchas notas de venta pueden pertenecer a un mismo Cliente
    @ManyToOne
    @JoinColumn(name = "id_cliente_fk", referencedColumnName = "idCliente", nullable = false) // Llave foránea en MySQL
    private Cliente cliente;

    // RELACIÓN DE AUDITORÍA: Muchas notas de venta pueden ser cobradas por un mismo
    // Empleado
    @ManyToOne
    @JoinColumn(name = "id_empleado_fk", referencedColumnName = "idEmpleado", nullable = false) // Llave foránea
    private Empleado empleado;

    @OneToMany(mappedBy = "notaVenta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleVenta> detalles;

    public NotaVenta() {
    }

    public NotaVenta(Integer idNotaVenta, Integer numeroFactura, LocalDateTime fechaEmision, BigDecimal totalPagar,
            Cliente cliente, Empleado empleado) {
        this.idNotaVenta = idNotaVenta;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.totalPagar = totalPagar;
        this.cliente = cliente;
        this.empleado = empleado;
    }

    public Integer getIdNotaVenta() {
        return idNotaVenta;
    }

    public void setIdNotaVenta(Integer idNotaVenta) {
        this.idNotaVenta = idNotaVenta;
    }

    public Integer getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Integer numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }
}