package com.example.muncel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.muncel.model.DetalleVenta;

import java.util.List;


@Repository

public interface DetalleVentaRepository extends JpaRepository <DetalleVenta, Long>{
    List<DetalleVenta> findByIdDetalle(Long idDetalle);
}
