package com.saludvida.api.repository;

import com.saludvida.api.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository; 
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    Optional<Paciente> findByDni(String dni);
    
    Page<Paciente> findByEstadoAndDniContaining(Paciente.Estado estado, String dni, Pageable pageable);
    Page<Paciente> findByEstadoAndNombresContainingIgnoreCaseOrEstadoAndApellidosContainingIgnoreCase(
            Paciente.Estado estado1, String nombres, Paciente.Estado estado2, String apellidos, Pageable pageable);

    Page<Paciente> findByEstado(Paciente.Estado estado, Pageable pageable);
}
