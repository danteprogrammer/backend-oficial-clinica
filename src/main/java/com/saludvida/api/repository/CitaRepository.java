package com.saludvida.api.repository;

import com.saludvida.api.model.Cita;
import com.saludvida.api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

    Page<Cita> findByPaciente_IdPaciente(Integer pacienteId, Pageable pageable);

    Page<Cita> findByFecha(LocalDate fecha, Pageable pageable);

    List<Cita> findByEstado(Cita.Estado estado);

    List<Cita> findByFechaAndHora(LocalDate fecha, LocalTime hora);
}
