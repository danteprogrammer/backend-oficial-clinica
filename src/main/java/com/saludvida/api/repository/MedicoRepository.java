package com.saludvida.api.repository;

import com.saludvida.api.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {

    List<Medico> findByEstado(Medico.Estado estado);

    Optional<Medico> findByDni(String dni);

    List<Medico> findByEspecialidad(String especialidad);

    List<Medico> findByEstadoAndEspecialidad(Medico.Estado estado, String especialidad);

    @Query("SELECT DISTINCT m.especialidad FROM Medico m")
    List<String> findDistinctEspecialidades();
}
