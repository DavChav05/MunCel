package com.example.muncel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.muncel.model.Cliente;
import com.example.muncel.model.NotaVenta;

import java.util.List;

@Repository

public interface NotaVentaRepository extends JpaRepository <NotaVenta, Long> {

    List<NotaVenta> findByNumeroFactura(Long numeroFactura);

    List<NotaVenta> findByCliente(Cliente cliente);
}
