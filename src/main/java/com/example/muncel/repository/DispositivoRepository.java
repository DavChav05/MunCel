package com.example.muncel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.muncel.model.Dispositivo;

import java.util.List;

@Repository

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long>{

    List<Dispositivo> findByModelo(String modelo);

    List<Dispositivo> findByImeiOSeríe(String imeiOSeríe);    
} 
