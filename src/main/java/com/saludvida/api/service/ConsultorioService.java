package com.saludvida.api.service;

import com.saludvida.api.model.Consultorio;
import com.saludvida.api.model.Especialidad;
import com.saludvida.api.repository.ConsultorioRepository;
import com.saludvida.api.repository.EspecialidadRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultorioService {

    private final ConsultorioRepository consultorioRepository;
    private final EspecialidadRepository especialidadRepository;

    @Transactional(readOnly = true)
    public List<Consultorio> obtenerTodos() {
        return consultorioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Consultorio> obtenerConsultoriosDisponibles() {
        return consultorioRepository.findConsultoriosDisponibles();
    }

    @Transactional(readOnly = true)
    public Optional<Consultorio> obtenerPorId(Integer id) {
        return consultorioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean estaDisponible(Integer id) {
        return consultorioRepository.findById(id)
                .map(consultorio -> consultorio.getEstado() == Consultorio.Estado.Disponible)
                .orElse(false);
    }

    @Transactional
    public Consultorio actualizarEstado(Integer id, Consultorio.Estado nuevoEstado) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado con ID: " + id));

        consultorio.setEstado(nuevoEstado);
        return consultorioRepository.save(consultorio);
    }

    @Transactional(readOnly = true)
    public List<Consultorio> obtenerPorEstado(Consultorio.Estado estado) {
        return consultorioRepository.findByEstado(estado);
    }

    @Transactional
    public Consultorio crearConsultorio(Consultorio consultorio) {
        if (consultorio.getEspecialidad() == null || consultorio.getEspecialidad().getIdEspecialidad() == null) {
            throw new IllegalArgumentException("La especialidad es obligatoria para crear un consultorio.");
        }
        Especialidad especialidad = especialidadRepository.findById(consultorio.getEspecialidad().getIdEspecialidad())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Especialidad no encontrada con ID: " + consultorio.getEspecialidad().getIdEspecialidad()));

        consultorio.setEspecialidad(especialidad);

        if (consultorio.getEstado() == null) {
            consultorio.setEstado(Consultorio.Estado.Disponible);
        }
        return consultorioRepository.save(consultorio);
    }

    @Transactional
    public Consultorio actualizarConsultorio(Integer id, Consultorio consultorioData) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consultorio no encontrado con ID: " + id));

        if (consultorioData.getEspecialidad() == null
                || consultorioData.getEspecialidad().getIdEspecialidad() == null) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
        Especialidad especialidad = especialidadRepository
                .findById(consultorioData.getEspecialidad().getIdEspecialidad())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Especialidad no encontrada con ID: " + consultorioData.getEspecialidad().getIdEspecialidad()));

        consultorio.setNumero(consultorioData.getNumero());
        consultorio.setPiso(consultorioData.getPiso());
        consultorio.setDescripcion(consultorioData.getDescripcion());
        consultorio.setEstado(consultorioData.getEstado());
        consultorio.setEspecialidad(especialidad); 

        return consultorioRepository.save(consultorio);
    }
    // FIN: CÓDIGO NUEVO

    @Transactional
    public void eliminarConsultorio(Integer id) {
        if (!consultorioRepository.existsById(id)) {
            throw new RuntimeException("Consultorio no encontrado con ID: " + id);
        }
        consultorioRepository.deleteById(id);
    }
}
