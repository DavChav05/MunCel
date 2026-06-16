package com.example.muncel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "OrdenServicio")
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Equivale a AUTO_INCREMENT (Número de Orden)
    private Integer idOrden;

    @Column(name = "fecha_ingreso", nullable = false) // DATETIME DEFAULT CURRENT_TIMESTAMP
    private LocalDateTime fechaIngreso = LocalDateTime.now();

    @Column(name = "falla_reportada", nullable = false, columnDefinition = "TEXT") // TEXT NOT NULL
    private String fallaReportada;

    @Column(name = "numero_orden", unique = true)
    private Integer numeroOrden;

    @Column(name = "observaciones_fisicas", columnDefinition = "TEXT") // TEXT (nullable = true por defecto)
    private String observacionesFisicas;

    @Column(name = "presupuesto_estimado", nullable = false, precision = 10, scale = 2) // DECIMAL(10,2) DEFAULT 0.00
    private BigDecimal presupuestoEstimado = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING) // Mapea el ENUM de MySQL como String en Java
    @Column(nullable = false, length = 20)
    private EstadoOrden estado = EstadoOrden.INGRESADO; // DEFAULT 'INGRESADO'

    // RELACIÓN: Muchas órdenes de servicio pueden pertenecer a un mismo Dispositivo
    @ManyToOne
    @JoinColumn(name = "id_dispositivo_fk", referencedColumnName = "idDispositivo", nullable = false)
    private Dispositivo dispositivo;

    // RELACIÓN DE AUDITORÍA: Muchas órdenes son gestionadas por un Empleado
    // (Técnico/Admin)
    @ManyToOne
    @JoinColumn(name = "id_empleado_fk", referencedColumnName = "idEmpleado", nullable = false)
    private Empleado empleado;

    public OrdenServicio() {
    }

    public OrdenServicio(Integer idOrden, LocalDateTime fechaIngreso, String fallaReportada, Integer numeroOrden,
            String observacionesFisicas, BigDecimal presupuestoEstimado, EstadoOrden estado, Dispositivo dispositivo,
            Empleado empleado) {
        this.idOrden = idOrden;
        this.fechaIngreso = fechaIngreso;
        this.fallaReportada = fallaReportada;
        this.numeroOrden = numeroOrden;
        this.observacionesFisicas = observacionesFisicas;
        this.presupuestoEstimado = presupuestoEstimado;
        this.estado = estado;
        this.dispositivo = dispositivo;
        this.empleado = empleado;
    }

    public Integer getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Integer idOrden) {
        this.idOrden = idOrden;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFallaReportada() {
        return fallaReportada;
    }

    public void setFallaReportada(String fallaReportada) {
        this.fallaReportada = fallaReportada;
    }

    public Integer getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(Integer numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getObservacionesFisicas() {
        return observacionesFisicas;
    }

    public void setObservacionesFisicas(String observacionesFisicas) {
        this.observacionesFisicas = observacionesFisicas;
    }

    public BigDecimal getPresupuestoEstimado() {
        return presupuestoEstimado;
    }

    public void setPresupuestoEstimado(BigDecimal presupuestoEstimado) {
        this.presupuestoEstimado = presupuestoEstimado;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

}