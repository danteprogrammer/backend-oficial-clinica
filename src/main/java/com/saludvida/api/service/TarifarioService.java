package com.saludvida.api.service;

import com.saludvida.api.model.Especialidad;
import com.saludvida.api.model.Tarifario;
import com.saludvida.api.repository.EspecialidadRepository;
import com.saludvida.api.repository.TarifarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TarifarioService {

    private final TarifarioRepository tarifarioRepository;
    private final EspecialidadRepository especialidadRepository;

    @Transactional(readOnly = true)
    public List<Tarifario> listarTodos() {
        return tarifarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tarifario buscarPorId(Integer id) {
        return tarifarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarifa no encontrada con ID: " + id));
    }

    @Transactional
    public Tarifario crearTarifa(Tarifario tarifario) {
        if (tarifario.getEspecialidad() == null || tarifario.getEspecialidad().getIdEspecialidad() == null) {
            throw new IllegalStateException("Debe proporcionar un ID de especialidad.");
        }
        Especialidad esp = especialidadRepository.findById(tarifario.getEspecialidad().getIdEspecialidad())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Especialidad no encontrada con ID: " + tarifario.getEspecialidad().getIdEspecialidad()));
        tarifario.setEspecialidad(esp);

        Optional<Tarifario> existente = tarifarioRepository.findByEspecialidad(esp);
        if (existente.isPresent()) {
            throw new IllegalStateException(
                    "Ya existe una tarifa para la especialidad: " + esp.getNombre());
        }
        return tarifarioRepository.save(tarifario);
    }

    @Transactional
    public Tarifario actualizarTarifa(Integer id, Tarifario tarifaActualizada) {
        Tarifario tarifaExistente = buscarPorId(id);

        if (tarifaActualizada.getEspecialidad() == null
                || tarifaActualizada.getEspecialidad().getIdEspecialidad() == null) {
            throw new IllegalStateException("Debe proporcionar un ID de especialidad.");
        }
        Especialidad espActualizada = especialidadRepository
                .findById(tarifaActualizada.getEspecialidad().getIdEspecialidad())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Especialidad no encontrada con ID: "
                                + tarifaActualizada.getEspecialidad().getIdEspecialidad()));

        if (!tarifaExistente.getEspecialidad().equals(espActualizada)) {
            Optional<Tarifario> otraTarifa = tarifarioRepository
                    .findByEspecialidad(espActualizada); 
            if (otraTarifa.isPresent() && !otraTarifa.get().getId().equals(id)) {
                throw new IllegalStateException("La especialidad '" + espActualizada.getNombre() // Corregido
                        + "' ya está asignada a otra tarifa.");
            }
        }

        tarifaExistente.setEspecialidad(espActualizada);
        tarifaExistente.setPrecio(tarifaActualizada.getPrecio());

        return tarifarioRepository.save(tarifaExistente);
    }

    @Transactional
    public void eliminarTarifa(Integer id) {
        if (!tarifarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Tarifa no encontrada con ID: " + id);
        }
        tarifarioRepository.deleteById(id);
    }
}