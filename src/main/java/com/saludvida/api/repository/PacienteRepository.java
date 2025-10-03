package com.saludvida.api.repository;

import com.saludvida.api.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    Optional<Paciente> findByDni(String dni);

    // Método de búsqueda mejorado que busca en DNI, nombres y apellidos
    @Query("SELECT p FROM Paciente p WHERE p.estado = :estado AND (p.dni LIKE %:termino% OR UPPER(p.nombres) LIKE UPPER(CONCAT('%', :termino, '%')) OR UPPER(p.apellidos) LIKE UPPER(CONCAT('%', :termino, '%')))")
    Page<Paciente> findByEstadoAndTermino(@Param("estado") Paciente.Estado estado, @Param("termino") String termino,
            Pageable pageable);

    // Métodos anteriores (se conservan por si se usan en otras partes)
    Page<Paciente> findByEstadoAndDniContaining(Paciente.Estado estado, String dni, Pageable pageable);

    Page<Paciente> findByEstadoAndNombresContainingIgnoreCaseOrEstadoAndApellidosContainingIgnoreCase(
            Paciente.Estado estado1, String nombres, Paciente.Estado estado2, String apellidos, Pageable pageable);

    Page<Paciente> findByEstado(Paciente.Estado estado, Pageable pageable);

    Page<Paciente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos,
            Pageable pageable);
}
