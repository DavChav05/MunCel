package com.example.muncel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.muncel.model.Empleado;
import java.util.List;
import java.util.Optional;


@Repository

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer>{

    Optional<Empleado> findByUsername(String username);

    List<Empleado> findByNombreCompleto(String nombreCompleto);

    Optional<Empleado> findByIdEmpleado(Integer idEmpleado);
}
