package com.saludvida.api.repository;

import com.saludvida.api.model.Horario;
import com.saludvida.api.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    
    // Este método ya existía
    List<Horario> findByMedico(Medico medico);

    // --- AÑADIR ESTE MÉTODO ---
    // Es más eficiente para buscar por ID desde el servicio
    List<Horario> findByMedico_IdMedico(Integer idMedico);
}