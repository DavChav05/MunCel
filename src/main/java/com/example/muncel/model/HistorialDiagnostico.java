package com.example.muncel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "HistorialDiagnostico")
public class HistorialDiagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Equivale a AUTO_INCREMENT
    private Integer idHistorial;

    @Column(name = "fecha_hora", nullable = false) // DATETIME DEFAULT CURRENT_TIMESTAMP
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(name = "accion_realizada", nullable = false, columnDefinition = "TEXT") // TEXT NOT NULL
    private String accionRealizada;

    // Nuevo campo para registrar el costo de la mano de obra o reparación aplicada
    @Column(name = "costos_reparacion", nullable = false, precision = 10, scale = 2)
    private BigDecimal costosReparacion = BigDecimal.ZERO;

    // RELACIÓN: Muchos historiales pertenecen a una misma Orden de Servicio
    @ManyToOne
    @JoinColumn(name = "id_orden_fk", referencedColumnName = "idOrden",nullable = false) // ON DELETE CASCADE se maneja a nivel de BD
    private OrdenServicio ordenServicio;

    //Un historial puede (o no) tener asociado un repuesto del inventario
    @ManyToOne
    @JoinColumn(name = "id_producto_fk", referencedColumnName = "idProducto",nullable = true) // Nullable porque no todo diagnóstico usa repuesto
    private Producto producto;

    public HistorialDiagnostico() {
    }

    public HistorialDiagnostico(Integer idHistorial, LocalDateTime fechaHora, String accionRealizada,
            BigDecimal costosReparacion, OrdenServicio ordenServicio, Producto producto) {
        this.idHistorial = idHistorial;
        this.fechaHora = fechaHora;
        this.accionRealizada = accionRealizada;
        this.costosReparacion = costosReparacion;
        this.ordenServicio = ordenServicio;
        this.producto = producto;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getAccionRealizada() {
        return accionRealizada;
    }

    public void setAccionRealizada(String accionRealizada) {
        this.accionRealizada = accionRealizada;
    }

    public BigDecimal getCostosReparacion() {
        return costosReparacion;
    }

    public void setCostosReparacion(BigDecimal costosReparacion) {
        this.costosReparacion = costosReparacion;
    }

    public OrdenServicio getOrdenServicio() {
        return ordenServicio;
    }

    public void setOrdenServicio(OrdenServicio ordenServicio) {
        this.ordenServicio = ordenServicio;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}