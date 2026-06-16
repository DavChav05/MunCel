package com.example.muncel.model;

public class DetalleVentaTemporal {
    
    // Atributos
    private Producto producto;
    private int cantidad;
    private double precioVenta;

    // Constructor vacío (necesario para Spring)
    public DetalleVentaTemporal() {
    }

    // Constructor con parámetros (opcional, pero útil)
    public DetalleVentaTemporal(Producto producto, int cantidad, double precioVenta) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
    }

    // Getters y Setters (Esto es lo que reemplaza al @Data de Lombok)
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
}