package com.example.muncel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.muncel.model.Empleado;
import java.util.List;


@Repository

public interface EmpleadoRepository extends JpaRepository<Empleado, Long>{

    List<Empleado> findByUsername(String username);

    List<Empleado> findByNombreCompleto(String nombreCompleto);
}
