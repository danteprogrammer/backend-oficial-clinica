package com.saludvida.api.service;

import com.saludvida.api.model.Cita;
import com.saludvida.api.model.Cita.Estado;
import com.saludvida.api.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;

    public Page<Cita> listarCitas(Pageable pageable) {
        return citaRepository.findAll(pageable);
    }

    public Cita obtenerCitaPorId(Integer id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + id));
    }

    @Transactional
    public Cita registrarCita(Cita cita) {
        cita.setEstado(Estado.Pendiente);
        return citaRepository.save(cita);
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

    public Page<Cita> buscarCitasPorFecha(java.time.LocalDate fecha, Pageable pageable) {
        return citaRepository.findByFecha(fecha, pageable);
    }

    public List<Cita> listarCitasPorEstado(Estado estado) {
        return citaRepository.findByEstado(estado);
    }
}
