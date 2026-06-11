package com.example.muncel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.muncel.model.ClienteUsuario;
import java.util.Optional;

@Repository
public interface ClienteUsuarioRepository extends JpaRepository<ClienteUsuario, Integer> { // 1. CAMBIADO A INTEGER

    // 2. Corregido para que busque un ClienteUsuario por su username (Spring Security lo usará después)
    Optional<ClienteUsuario> findOptionalByUsername(String username);

    // 3. Este método ya está perfecto para tu validación del controlador
    boolean existsByUsername(String username);
}