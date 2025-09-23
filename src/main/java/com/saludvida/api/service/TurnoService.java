package com.saludvida.api.service;

import com.saludvida.api.dto.TurnoDto;
import com.saludvida.api.dto.TurnoResponseDto;
import com.saludvida.api.model.Consultorio;
import com.saludvida.api.model.Paciente;
import com.saludvida.api.model.Turno;
import com.saludvida.api.repository.ConsultorioRepository;
import com.saludvida.api.repository.PacienteRepository;
import com.saludvida.api.repository.TurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultorioRepository consultorioRepository;

    @Transactional(readOnly = true)
    public List<TurnoResponseDto> obtenerTodosLosTurnos() {
        return turnoRepository.findAll().stream()
                .map(TurnoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TurnoResponseDto> obtenerTurnosPorPaciente(Integer pacienteId) {
        return turnoRepository.findByPacienteIdPaciente(pacienteId).stream()
                .map(TurnoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TurnoResponseDto> obtenerTurnosPorConsultorio(Integer consultorioId) {
        return turnoRepository.findByConsultorioIdConsultorio(consultorioId).stream()
                .map(TurnoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TurnoResponseDto> obtenerTurnosPorEstado(Turno.Estado estado) {
        return turnoRepository.findByEstado(estado).stream()
                .map(TurnoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TurnoResponseDto asignarTurno(TurnoDto turnoDto) {
        // Validar que el paciente existe
        Paciente paciente = pacienteRepository.findById(turnoDto.getPacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Paciente no encontrado con ID: " + turnoDto.getPacienteId()));

        // Validar que el consultorio existe
        Consultorio consultorio = consultorioRepository.findById(turnoDto.getConsultorioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Consultorio no encontrado con ID: " + turnoDto.getConsultorioId()));

        // Validar que el consultorio esté disponible
        if (consultorio.getEstado() != Consultorio.Estado.Disponible) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El consultorio no está disponible");
        }

        // Validar que no haya conflicto de horario
        turnoRepository.findConflictingTurno(turnoDto.getFecha(), turnoDto.getHora(), turnoDto.getConsultorioId())
                .ifPresent(conflictingTurno -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Ya existe un turno en ese horario para el consultorio");
                });

        // Crear el turno
        Turno turno = Turno.builder()
                .paciente(paciente)
                .consultorio(consultorio)
                .fecha(turnoDto.getFecha())
                .hora(turnoDto.getHora())
                .motivo(turnoDto.getMotivo())
                .observaciones(turnoDto.getObservaciones())
                .estado(Turno.Estado.Pendiente)
                .build();

        Turno turnoGuardado = turnoRepository.save(turno);
        return TurnoResponseDto.fromEntity(turnoGuardado);
    }

    @Transactional
    public TurnoResponseDto cambiarEstadoTurno(Integer turnoId, String nuevoEstado) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Turno no encontrado con ID: " + turnoId));

        try {
            Turno.Estado estado = Turno.Estado.valueOf(nuevoEstado);
            turno.setEstado(estado);

            // Si el turno pasa a "EnProceso", marcar consultorio como ocupado
            if (estado == Turno.Estado.EnProceso) {
                turno.getConsultorio().setEstado(Consultorio.Estado.Ocupado);
                consultorioRepository.save(turno.getConsultorio());
            }

            // Si el turno se completa o cancela, liberar consultorio
            if (estado == Turno.Estado.Completado || estado == Turno.Estado.Cancelado) {
                turno.getConsultorio().setEstado(Consultorio.Estado.Disponible);
                consultorioRepository.save(turno.getConsultorio());
            }

            Turno turnoActualizado = turnoRepository.save(turno);
            return TurnoResponseDto.fromEntity(turnoActualizado);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estado no válido: " + nuevoEstado);
        }
    }

    @Transactional
    public void cancelarTurno(Integer turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Turno no encontrado con ID: " + turnoId));

        turno.setEstado(Turno.Estado.Cancelado);

        // Liberar consultorio
        turno.getConsultorio().setEstado(Consultorio.Estado.Disponible);
        consultorioRepository.save(turno.getConsultorio());

        turnoRepository.save(turno);
    }

    @Transactional(readOnly = true)
    public List<TurnoResponseDto> obtenerTurnosPorFecha(LocalDate fecha) {
        return turnoRepository.findAll().stream()
                .filter(turno -> turno.getFecha().equals(fecha))
                .map(TurnoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
