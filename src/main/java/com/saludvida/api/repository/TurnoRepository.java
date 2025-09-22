package com.saludvida.api.repository;

import com.saludvida.api.model.Consultorio;
import com.saludvida.api.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    // Método para encontrar turnos por una fecha posterior a la fecha actual y ordenarlos
    List<Turno> findByFechaAfterOrderByFechaAscHoraAsc(LocalDate fecha);

    // Método para verificar conflictos de horarios en un consultorio específico
    List<Turno> findByConsultorioAndFechaAndHora(Consultorio consultorio, LocalDate fecha, LocalTime hora);

    // Método para encontrar turnos por consultorio y fecha
    List<Turno> findByConsultorioAndFecha(Consultorio consultorio, LocalDate fecha);

    // Método para encontrar turnos por paciente
    List<Turno> findByPacienteIdOrderByFechaDescHoraDesc(Integer pacienteId);

    // Método para encontrar turnos por estado
    List<Turno> findByEstado(Turno.Estado estado);

    // Método para contar turnos activos en un consultorio para una fecha
    @Query("SELECT COUNT(t) FROM Turno t WHERE t.consultorio = :consultorio AND t.fecha = :fecha " +
           "AND t.estado IN ('Pendiente', 'Confirmado', 'EnProceso')")
    long countTurnosActivosPorConsultorioYFecha(@Param("consultorio") Consultorio consultorio,
                                               @Param("fecha") LocalDate fecha);
}
