package com.example.muncel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @Column(name = "codigo_producto", length = 50, unique = true)
    private String codigoProducto;

    @Column(name = "nombre_producto", length = 100, nullable = false)
    private String nombreProducto;

    @Column(name = "marca_producto", length = 100, nullable = false)
    private String marcaProducto;

    @Column(name = "precio_costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCosto;

    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private Integer stock = 0;

    // --- NUEVO: CATEGORÍA GENERAL ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaProducto categoria; // ACCESORIO, REPUESTO, etc.

    // --- NUEVO: TIPO ESPECÍFICO (Resuelve lo de los tipos de estuches) ---
    @Enumerated(EnumType.STRING)
    @Column(name = "subcategoria", nullable = false, length = 50)
    private SubcategoriaProducto subcategoria; // CARGADOR, AUDIFONOS, CASE_TRANSPARENTE, CASE_DISEÑO

    @Column(name = "detalle_producto", length = 100, nullable = true)
    private String detalleProducto; // detalles de diferencia entre productos similares

    @Column(name = "visible_en_catalogo", nullable = false)
    private Boolean visibleEnCatalogo = true; // Por defecto todos son visibles, a menos que indiques lo contrario

    // --- NUEVO: RUTA DE LA IMAGEN PARA EL CATÁLOGO ---
    @Column(name = "url_imagen", length = 255, nullable = true)
    private String urlImagen; // Guarda la ruta: "/img/catalogo/case_transparente.png"

    @ManyToOne
    @JoinColumn(name = "id_proveedor_fk", referencedColumnName = "idProveedor", nullable = false)
    private Proveedor proveedor;

    public Producto() {
    }

    public Producto(Integer idProducto, String codigoProducto, String nombreProducto, String marcaProducto,
            BigDecimal precioCosto, BigDecimal precioVenta, Integer stock, CategoriaProducto categoria,
            SubcategoriaProducto subcategoria, String detalleProducto, Boolean visibleEnCatalogo, String urlImagen,
            Proveedor proveedor) {
        this.idProducto = idProducto;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.marcaProducto = marcaProducto;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.detalleProducto = detalleProducto;
        this.visibleEnCatalogo = visibleEnCatalogo;
        this.urlImagen = urlImagen;
        this.proveedor = proveedor;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getMarcaProducto() {
        return marcaProducto;
    }

    public void setMarcaProducto(String marcaProducto) {
        this.marcaProducto = marcaProducto;
    }

    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }

    public SubcategoriaProducto getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(SubcategoriaProducto subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getDetalleProducto() {
        return detalleProducto;
    }

    public void setDetalleProducto(String detalleProducto) {
        this.detalleProducto = detalleProducto;
    }

    public Boolean getVisibleEnCatalogo() {
        return visibleEnCatalogo;
    }

    public void setVisibleEnCatalogo(Boolean visibleEnCatalogo) {
        this.visibleEnCatalogo = visibleEnCatalogo;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    
}