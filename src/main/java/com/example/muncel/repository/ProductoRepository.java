package com.example.muncel.repository;

import com.example.muncel.model.Producto;
import com.example.muncel.model.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Para la página principal: Filtra de golpe todo lo oculto
    List<Producto> findByVisibleEnCatalogoTrue();

    // Para los filtros: Busca usando el tipo ENUM directamente
    List<Producto> findByCategoria(CategoriaProducto categoria);
}