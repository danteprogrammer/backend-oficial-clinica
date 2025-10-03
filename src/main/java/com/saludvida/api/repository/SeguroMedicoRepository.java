package com.saludvida.api.repository;

import com.saludvida.api.model.SeguroMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SeguroMedicoRepository extends JpaRepository<SeguroMedico, Integer> {
    Optional<SeguroMedico> findByPaciente_IdPaciente(Integer idPaciente);
}
