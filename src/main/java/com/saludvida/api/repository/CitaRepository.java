package com.saludvida.api.repository;

import com.saludvida.api.model.Cita;
import com.saludvida.api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.saludvida.api.dto.EspecialidadConteoDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

    Page<Cita> findByPaciente_IdPaciente(Integer pacienteId, Pageable pageable);

    Page<Cita> findByFecha(LocalDate fecha, Pageable pageable);

    List<Cita> findByEstado(Cita.Estado estado);

    List<Cita> findByFechaAndHora(LocalDate fecha, LocalTime hora);

    List<Cita> findByPacienteIdPacienteAndEstadoPagoAndFecha(Integer idPaciente, Cita.EstadoPago estadoPago,
            LocalDate fecha);

    List<Cita> findByPacienteIdPacienteAndEstadoPago(Integer idPaciente, Cita.EstadoPago estadoPago);

    @Query("SELECT COALESCE(SUM(t.precio), 0) " +
            "FROM Cita c " +
            "JOIN c.medico m " +
            "JOIN Tarifario t ON m.especialidad = t.especialidad " +
            "WHERE c.fecha = :fecha AND c.estadoPago = 'PAGADO'")
    BigDecimal sumIngresosPorFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT COUNT(c) " +
            "FROM Cita c " +
            "WHERE c.fecha = :fecha AND c.estadoPago = 'PAGADO'")
    Long countCitasPagadasPorFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT new com.saludvida.api.dto.EspecialidadConteoDto(m.especialidad, COUNT(c)) " +
            "FROM Cita c " +
            "JOIN c.medico m " +
            "WHERE c.fecha = :fecha AND c.estadoPago = 'PAGADO' " +
            "GROUP BY m.especialidad")
    List<EspecialidadConteoDto> countConsultasPorEspecialidadHoy(@Param("fecha") LocalDate fecha);

    List<Cita> findAllByEstadoPagoOrderByFechaDesc(Cita.EstadoPago estadoPago);
}
