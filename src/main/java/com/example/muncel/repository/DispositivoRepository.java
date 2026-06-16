package com.example.muncel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.muncel.model.DetalleVenta;
import com.example.muncel.model.Dispositivo;

import java.util.List;
import java.util.Optional;

@Repository

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long>{

    List<Dispositivo> findByModelo(String modelo);

    Optional<Dispositivo> findByImeiOSeríe(String imeiOSeríe);  
    
    @Query("SELECT d FROM DetalleVenta d LEFT JOIN FETCH d.producto WHERE d.notaVenta IS NULL")
    List<DetalleVenta> findByNotaVentaIsNull();
} 
