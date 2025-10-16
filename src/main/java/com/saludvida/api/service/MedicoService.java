package com.saludvida.api.service;

import com.saludvida.api.model.Horario;
import com.saludvida.api.model.Medico;
import com.saludvida.api.repository.HorarioRepository;
import com.saludvida.api.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final HorarioRepository horarioRepository;

    @Transactional(readOnly = true)
    public List<Medico> obtenerTodosLosMedicos() {
        return medicoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Medico> obtenerMedicoPorId(Integer id) {
        return medicoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosDisponibles() {
        return medicoRepository.findByEstado(Medico.Estado.Activo);
    }

    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosPorEspecialidad(String especialidad) {
        return medicoRepository.findByEspecialidad(especialidad);
    }

@Transactional(readOnly = true)
    public Map<String, List<String>> obtenerHorarioMedico(Integer id) {
        List<Horario> horarios = horarioRepository.findByMedicoIdMedico(id);
        Map<String, List<String>> calendario = new LinkedHashMap<>();
        LocalDate hoy = LocalDate.now();
        int duracionCita = 30; // Duración de cada cita en minutos

        // Generar disponibilidad para los próximos 15 días
        for (int i = 0; i < 15; i++) {
            LocalDate fecha = hoy.plusDays(i);
            for (Horario horario : horarios) {
                if (horario.getDiaSemana() == fecha.getDayOfWeek()) {
                    List<String> horasDisponibles = new ArrayList<>();
                    LocalTime horaActual = horario.getHoraInicio();
                    while (horaActual.isBefore(horario.getHoraFin())) {
                        horasDisponibles.add(horaActual.toString());
                        horaActual = horaActual.plusMinutes(duracionCita);
                    }
                    if (!horasDisponibles.isEmpty()) {
                        calendario.put(fecha.toString(), horasDisponibles);
                    }
                }
            }
        }
        return calendario;
    }

    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosPorEstado(Medico.Estado estado) {
        return medicoRepository.findByEstado(estado);
    }

    @Transactional
    public Medico crearMedico(Medico medico) {
        // Validar que el DNI no esté duplicado
        Optional<Medico> medicoExistente = medicoRepository.findByDni(medico.getDni());
        if (medicoExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe un médico con el DNI: " + medico.getDni());
        }

        // Validar campos requeridos
        if (medico.getDni() == null || medico.getDni().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El DNI es obligatorio");
        }

        if (medico.getNombres() == null || medico.getNombres().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los nombres son obligatorios");
        }

        if (medico.getApellidos() == null || medico.getApellidos().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los apellidos son obligatorios");
        }

        if (medico.getEspecialidad() == null || medico.getEspecialidad().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La especialidad es obligatoria");
        }

        medico.setEstado(Medico.Estado.Activo);
        return medicoRepository.save(medico);
    }

    @Transactional
    public Medico actualizarMedico(Integer id, Medico medicoActualizado) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Médico no encontrado con ID: " + id));

        // Validar que el DNI no esté duplicado si se está cambiando
        if (!medico.getDni().equals(medicoActualizado.getDni())) {
            Optional<Medico> medicoConMismoDni = medicoRepository.findByDni(medicoActualizado.getDni());
            if (medicoConMismoDni.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ya existe otro médico con el DNI: " + medicoActualizado.getDni());
            }
        }

        // Actualizar campos
        if (medicoActualizado.getDni() != null) {
            medico.setDni(medicoActualizado.getDni());
        }
        if (medicoActualizado.getNombres() != null) {
            medico.setNombres(medicoActualizado.getNombres());
        }
        if (medicoActualizado.getApellidos() != null) {
            medico.setApellidos(medicoActualizado.getApellidos());
        }
        if (medicoActualizado.getSexo() != null) {
            medico.setSexo(medicoActualizado.getSexo());
        }
        if (medicoActualizado.getEspecialidad() != null) {
            medico.setEspecialidad(medicoActualizado.getEspecialidad());
        }
        if (medicoActualizado.getTelefono() != null) {
            medico.setTelefono(medicoActualizado.getTelefono());
        }
        if (medicoActualizado.getEmail() != null) {
            medico.setEmail(medicoActualizado.getEmail());
        }
        if (medicoActualizado.getLicenciaMedica() != null) {
            medico.setLicenciaMedica(medicoActualizado.getLicenciaMedica());
        }
        if (medicoActualizado.getEstado() != null) {
            medico.setEstado(medicoActualizado.getEstado());
        }

        return medicoRepository.save(medico);
    }

    @Transactional
    public void eliminarMedico(Integer id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Médico no encontrado con ID: " + id));

        medicoRepository.delete(medico);
    }

    @Transactional
    public Medico cambiarEstadoMedico(Integer id, Medico.Estado nuevoEstado) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Médico no encontrado con ID: " + id));

        medico.setEstado(nuevoEstado);
        return medicoRepository.save(medico);
    }

    @Transactional(readOnly = true)
    public List<String> obtenerEspecialidades() {
        return medicoRepository.findDistinctEspecialidades();
    }
}
