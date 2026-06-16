package com.example.muncel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "DetalleVenta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Equivale a AUTO_INCREMENT
    private Integer idDetalle;

    @Column(nullable = false) // INT NOT NULL
    private Integer cantidad;

    // Usamos BigDecimal para asegurar un cálculo de dinero exacto
    @Column(name = "precio_unitario_historico", nullable = false, precision = 10, scale = 2) // DECIMAL(10,2) NOT NULL
    private BigDecimal precioUnitarioHistorico;

    @Column(name = "subtotal_linea", nullable = false, precision = 10, scale = 2) // DECIMAL(10,2) NOT NULL
    private BigDecimal subtotalLinea;

    // RELACIÓN: Muchos detalles pertenecen a una misma Nota de Venta
    // El CascadeType.REMOVE o el ajuste en la base de datos asegura el ON DELETE
    // CASCADE
    @ManyToOne
    @JoinColumn(name = "id_nota_venta_fk", referencedColumnName = "idNotaVenta",nullable = true)
    private NotaVenta notaVenta;

    // RELACIÓN: Muchos detalles pueden registrar el mismo Producto (en diferentes
    // notas)
    @ManyToOne
    @JoinColumn(name = "id_producto_fk", referencedColumnName = "idProducto", nullable = false) // Llave foránea en MySQL
    private Producto producto;

    public DetalleVenta() {
    }

    public DetalleVenta(Integer idDetalle, Integer cantidad, BigDecimal precioUnitarioHistorico,
            BigDecimal subtotalLinea, NotaVenta notaVenta, Producto producto) {
        this.idDetalle = idDetalle;
        this.cantidad = cantidad;
        this.precioUnitarioHistorico = precioUnitarioHistorico;
        this.subtotalLinea = this.precioUnitarioHistorico.multiply(new BigDecimal(this.cantidad));
        this.notaVenta = notaVenta;
        this.producto = producto;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitarioHistorico() {
        return precioUnitarioHistorico;
    }

    public void setPrecioUnitarioHistorico(BigDecimal precioUnitarioHistorico) {
        this.precioUnitarioHistorico = precioUnitarioHistorico;
    }

    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }

    public void setSubtotalLinea(BigDecimal subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
    }

    public NotaVenta getNotaVenta() {
        return notaVenta;
    }

    public void setNotaVenta(NotaVenta notaVenta) {
        this.notaVenta = notaVenta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

}