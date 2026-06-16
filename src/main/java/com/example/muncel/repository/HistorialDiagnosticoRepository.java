    package com.example.muncel.repository;

    import java.util.List;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import com.example.muncel.model.HistorialDiagnostico;

    @Repository

    public interface HistorialDiagnosticoRepository extends JpaRepository<HistorialDiagnostico, Long>{

        List<HistorialDiagnostico> findByAccionRealizada(String accionRealizada);
    }
