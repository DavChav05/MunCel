package com.example.muncel.repository;

import com.example.muncel.model.Cliente;
import com.example.muncel.model.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Integer> {

    // Spring Data JPA arma el SQL automático cruzando las tablas dispositivo y clientes
List<OrdenServicio> findByDispositivoCliente(Cliente cliente);
}