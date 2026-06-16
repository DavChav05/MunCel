package com.example.muncel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.muncel.model.DetalleVenta; // <-- Este import evita las letras rojas

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

    @Query("SELECT d FROM DetalleVenta d JOIN FETCH d.producto WHERE d.notaVenta IS NULL")
    List<DetalleVenta> findByNotaVentaIsNull();
}