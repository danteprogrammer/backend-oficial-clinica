package com.saludvida.api.repository;

import com.saludvida.api.model.HistoriaClinica;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Integer> {
    Optional<HistoriaClinica> findByPaciente_IdPaciente(Integer idPaciente);
}
