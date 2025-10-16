package com.saludvida.api.repository;

import com.saludvida.api.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    List<Horario> findByMedicoIdMedico(Integer idMedico);
}
