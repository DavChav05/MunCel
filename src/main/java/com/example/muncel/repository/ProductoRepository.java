package com.example.muncel.repository;

import com.example.muncel.model.Producto;
import com.example.muncel.model.SubcategoriaProducto;
import com.example.muncel.model.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Para la página principal: Filtra de golpe todo lo oculto
    List<Producto> findByVisibleEnCatalogoTrue();

    // Para los filtros: Busca usando el tipo ENUM directamente
    List<Producto> findByCategoria(CategoriaProducto categoria);

    // Cambia el método anterior por este para buscar por el ENUM
    // SubcategoriaProducto
    List<Producto> findBySubcategoria(SubcategoriaProducto subcategoria);

    Optional<Producto> findByCodigoProducto(String codigoProducto);
}