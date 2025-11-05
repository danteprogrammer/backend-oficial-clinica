package com.saludvida.api.repository;

import com.saludvida.api.model.Triaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriajeRepository extends JpaRepository<Triaje, Long> {

    // Método para buscar todos los registros de triaje de una historia, ordenados
    // por fecha
    // CAMBIA "Long" A "Integer" AQUÍ
    List<Triaje> findByHistoriaClinica_IdHistoriaClinicaOrderByFechaRegistroDesc(Integer idHistoriaClinica);
}
