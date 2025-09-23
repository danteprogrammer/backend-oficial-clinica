package com.saludvida.api.repository;

import com.saludvida.api.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Integer> {

    List<Turno> findByPacienteIdPaciente(Integer pacienteId);

    List<Turno> findByConsultorioIdConsultorio(Integer consultorioId);

    List<Turno> findByEstado(Turno.Estado estado);

    @Query("SELECT t FROM Turno t WHERE t.consultorio.idConsultorio = :consultorioId AND t.fecha = :fecha AND t.estado != 'Cancelado'")
    List<Turno> findTurnosByConsultorioAndFecha(@Param("consultorioId") Integer consultorioId, @Param("fecha") LocalDate fecha);

    @Query("SELECT t FROM Turno t WHERE t.fecha = :fecha AND t.hora = :hora AND t.consultorio.idConsultorio = :consultorioId AND t.estado != 'Cancelado'")
    Optional<Turno> findConflictingTurno(@Param("fecha") LocalDate fecha, @Param("hora") LocalTime hora, @Param("consultorioId") Integer consultorioId);

    @Query("SELECT t FROM Turno t WHERE t.estado = 'EnProceso' AND t.consultorio.idConsultorio = :consultorioId")
    Optional<Turno> findActiveTurnoByConsultorio(@Param("consultorioId") Integer consultorioId);
}
