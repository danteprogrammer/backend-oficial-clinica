package com.saludvida.api.repository;

import com.saludvida.api.model.Tarifario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TarifarioRepository extends JpaRepository<Tarifario, Integer> {
    Optional<Tarifario> findByEspecialidad(String especialidad);
}
