package com.example.muncel.repository;

import com.example.muncel.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> { // CAMBIADO A INTEGER

    // 1. Corregido para que coincida con la variable 'cedulaRuc' de tu entidad
    Optional<Cliente> findByCedulaRuc(String cedulaRuc);

    // 2. Este ya está perfecto, coincide con 'nombreCompleto'
    List<Cliente> findByNombreCompletoContainingIgnoreCase(String nombreCompleto);
}