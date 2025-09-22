package com.saludvida.api.service;

import com.saludvida.api.dto.TurnoDto;
import com.saludvida.api.model.Consultorio;
import com.saludvida.api.model.Paciente;
import com.saludvida.api.model.Turno;
import com.saludvida.api.repository.ConsultorioRepository;
import com.saludvida.api.repository.PacienteRepository;
import com.saludvida.api.repository.TurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultorioRepository consultorioRepository;

    @Transactional
    public Turno registrarTurno(TurnoDto turnoDTO) {
        // Validar que el DTO no sea nulo
        if (turnoDTO == null) {
            throw new IllegalArgumentException("Los datos del turno no pueden ser nulos");
        }

        // Validar campos requeridos
        if (turnoDTO.getPacienteId() == null || turnoDTO.getConsultorioId() == null) {
            throw new IllegalArgumentException("El ID del paciente y consultorio son requeridos");
        }

        if (turnoDTO.getFecha() == null || turnoDTO.getHora() == null) {
            throw new IllegalArgumentException("La fecha y hora del turno son requeridas");
        }

        if (turnoDTO.getMotivo() == null || turnoDTO.getMotivo().trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo del turno es requerido");
        }

        // Validar que la fecha no sea en el pasado
        LocalDateTime fechaHoraTurno = LocalDateTime.of(turnoDTO.getFecha(), turnoDTO.getHora());
        if (fechaHoraTurno.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se pueden crear turnos en fechas pasadas");
        }

        // Validar horario de atención (8:00 - 18:00)
        LocalTime hora = turnoDTO.getHora();
        if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
            throw new IllegalArgumentException("El horario del turno debe estar entre las 8:00 y las 18:00");
        }

        Optional<Paciente> pacienteOptional = pacienteRepository.findById(turnoDTO.getPacienteId());
        Optional<Consultorio> consultorioOptional = consultorioRepository.findById(turnoDTO.getConsultorioId());

        if (pacienteOptional.isEmpty()) {
            throw new RuntimeException("Paciente no encontrado con ID: " + turnoDTO.getPacienteId());
        }

        if (consultorioOptional.isEmpty()) {
            throw new RuntimeException("Consultorio no encontrado con ID: " + turnoDTO.getConsultorioId());
        }

        Consultorio consultorio = consultorioOptional.get();

        // Verificar que el consultorio esté disponible
        if (consultorio.getEstado() != Consultorio.Estado.Disponible) {
            throw new IllegalStateException("El consultorio no está disponible para nuevos turnos");
        }

        // Verificar que no haya conflicto de horarios en ese consultorio
        List<Turno> turnosExistentes = turnoRepository.findByConsultorioAndFechaAndHora(
            consultorio, turnoDTO.getFecha(), turnoDTO.getHora());

        if (!turnosExistentes.isEmpty()) {
            throw new IllegalStateException("Ya existe un turno programado para este consultorio en la fecha y hora especificada");
        }

        Turno turno = new Turno();
        turno.setPaciente(pacienteOptional.get());
        turno.setConsultorio(consultorio);
        turno.setFecha(turnoDTO.getFecha());
        turno.setHora(turnoDTO.getHora());
        turno.setMotivo(turnoDTO.getMotivo());
        turno.setObservaciones(turnoDTO.getObservaciones());
        turno.setEstado(Turno.Estado.Pendiente);

        return turnoRepository.save(turno);
    }

    public List<Turno> obtenerTurnosProximos() {
        LocalDateTime now = LocalDateTime.now();
        // Obtiene turnos desde hoy en adelante y los ordena por fecha y hora
        return turnoRepository.findByFechaAfterOrderByFechaAscHoraAsc(LocalDate.now());
    }

    @Transactional
    public void cambiarEstadoTurno(Integer id, String nuevoEstado) {
        // Validar que el estado no sea nulo o vacío
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado del turno no puede ser nulo o vacío");
        }

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        try {
            Turno.Estado estado = Turno.Estado.valueOf(nuevoEstado);
            turno.setEstado(estado);
            turnoRepository.save(turno);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado +
                ". Estados válidos: " + List.of(Turno.Estado.values()));
        }
    }

    @Transactional
    public void cancelarTurno(Integer id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        turno.setEstado(Turno.Estado.Cancelado);
        turnoRepository.save(turno);
    }
}