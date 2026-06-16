package com.example.muncel.repository;

import com.example.muncel.model.Cliente;
import com.example.muncel.model.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Integer> {

    // Tu método actual para el dashboard sigue intacto:
    List<OrdenServicio> findByDispositivoCliente(Cliente cliente);

    // NUEVO: El método mágico para buscar por el número de orden visible
    Optional<OrdenServicio> findByNumeroOrden(Integer numeroOrden); 
    // (Usa Integer si tu número de orden es numérico)

}