package com.example.muncel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.muncel.model.Proveedor;

import java.util.List;

@Repository

public interface ProveedorRepository extends JpaRepository <Proveedor, Long>{

    List<Proveedor> findByNombre(String nombre);
}
