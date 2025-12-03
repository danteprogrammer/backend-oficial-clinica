package com.saludvida.api.service;

import com.saludvida.api.model.Cita;
import com.saludvida.api.model.Cita.Estado;
import com.saludvida.api.model.Consultorio;
import com.saludvida.api.model.Especialidad;
import com.saludvida.api.model.Horario;
import com.saludvida.api.model.Medico;
import com.saludvida.api.repository.CitaRepository;
import com.saludvida.api.repository.ConsultorioRepository;
import com.saludvida.api.repository.HorarioRepository;
import com.saludvida.api.repository.MedicoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaService {
    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultorioRepository consultorioRepository;
    private final HorarioRepository horarioRepository;

    public Page<Cita> listarCitas(Pageable pageable) {
        return citaRepository.findAll(pageable);
    }

    public Cita obtenerCitaPorId(Integer id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + id));
    }

    @Transactional
    public Cita registrarCita(Cita cita) {
        Medico medico = medicoRepository.findById(cita.getMedico().getIdMedico())
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado"));

        Especialidad especialidad = medico.getEspecialidad();

        Optional<Consultorio> consultorioAsignado = buscarConsultorioDisponible(especialidad, cita.getFecha(), cita.getHora());

        if (consultorioAsignado.isPresent()) {
            cita.setConsultorio(consultorioAsignado.get());
        } else {
            throw new RuntimeException(
                    "No hay consultorios de " + especialidad.getNombre()
                            + " disponibles para la fecha y hora seleccionada.");
        }

        cita.setTieneSeguro(cita.getTieneSeguro());
        cita.setEstado(Estado.Pendiente);
        cita.setEstadoPago(Cita.EstadoPago.PENDIENTE);
        return citaRepository.save(cita);
    }

    @Transactional
    public Cita actualizarCita(Integer id, Cita citaActualizada) {
        Cita citaExistente = obtenerCitaPorId(id);

        citaExistente.setMedico(citaActualizada.getMedico());
        citaExistente.setFecha(citaActualizada.getFecha());
        citaExistente.setHora(citaActualizada.getHora());
        citaExistente.setEstado(citaActualizada.getEstado());
        citaExistente.setTieneSeguro(citaActualizada.getTieneSeguro());

        if (!citaExistente.getFecha().equals(citaActualizada.getFecha())
                || !citaExistente.getHora().equals(citaActualizada.getHora())) {
             
            Medico medico = medicoRepository.findById(citaActualizada.getMedico().getIdMedico())
                    .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado"));

            Especialidad especialidad = medico.getEspecialidad();

            Optional<Consultorio> consultorioAsignado = buscarConsultorioDisponible(especialidad, citaActualizada.getFecha(), citaActualizada.getHora());

            citaExistente.setConsultorio(consultorioAsignado
                    .orElseThrow(
                            () -> new RuntimeException("No hay consultorios disponibles para la nueva fecha/hora.")));
        }

        return citaRepository.save(citaExistente);
    }

    private Optional<Consultorio> buscarConsultorioDisponible(Especialidad especialidad, LocalDate fecha, LocalTime hora) {
        List<Cita> citasOcupadas = citaRepository.findByFechaAndHora(fecha, hora);
         
        List<Integer> idsConsultoriosOcupados = citasOcupadas.stream()
                .map(c -> c.getConsultorio().getIdConsultorio())
                .collect(Collectors.toList());

        List<Consultorio> consultoriosDisponibles = consultorioRepository
                .findByEstado(Consultorio.Estado.Disponible);

        return consultoriosDisponibles.stream()
                .filter(c -> c.getEspecialidad().equals(especialidad))
                .filter(c -> !idsConsultoriosOcupados.contains(c.getIdConsultorio()))
                .findFirst();
    }

    @Transactional
    public Cita actualizarEstado(Integer id, Estado nuevoEstado) {
        Cita cita = obtenerCitaPorId(id);
        cita.setEstado(nuevoEstado);
        return citaRepository.save(cita);
    }

    @Transactional
    public Cita cancelarCita(Integer id) {
        return actualizarEstado(id, Estado.Cancelada);
    }

    @Transactional
    public Cita completarCita(Integer id) {
        return actualizarEstado(id, Estado.Completada);
    }

    public Page<Cita> buscarCitasPorPaciente(Integer pacienteId, Pageable pageable) {
        return citaRepository.findByPaciente_IdPaciente(pacienteId, pageable);
    }

    public Page<Cita> buscarCitasPorFecha(LocalDate fecha, Pageable pageable) {
        return citaRepository.findByFecha(fecha, pageable);
    }

    public List<Cita> listarCitasPorEstado(Estado estado) {
        return citaRepository.findByEstado(estado);
    }

    public List<String> getHorasDisponibles(Integer idMedico, LocalDate fecha) {
        String diaSemana = fecha.getDayOfWeek().toString();

        List<Horario> horarios = horarioRepository.findByMedico_IdMedicoAndDiaSemana(idMedico, diaSemana);

        if (horarios.isEmpty()) {
            return new ArrayList<>();
        }

        List<LocalTime> slotsDisponibles = new ArrayList<>();
        final int DURACION_SLOT = 30;

        for (Horario horario : horarios) {
            LocalTime horaInicio = horario.getHoraInicio();
            LocalTime horaFin = horario.getHoraFin();

            LocalTime horaActual = horaInicio;
            while (horaActual.isBefore(horaFin)) {
                slotsDisponibles.add(horaActual);
                horaActual = horaActual.plusMinutes(DURACION_SLOT);
            }
        }

        List<Cita> citasReservadas = citaRepository.findByMedicoIdMedicoAndFecha(idMedico, fecha);
        List<LocalTime> horasReservadas = citasReservadas.stream()
                .map(Cita::getHora)
                .collect(Collectors.toList());

        slotsDisponibles.removeAll(horasReservadas);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return slotsDisponibles.stream()
                .map(slot -> slot.format(formatter))
                .collect(Collectors.toList());
    }
}