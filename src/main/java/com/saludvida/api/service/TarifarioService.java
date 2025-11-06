package com.saludvida.api.service;

import com.saludvida.api.model.Tarifario;
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
        // Validar que la especialidad no exista ya
        Optional<Tarifario> existente = tarifarioRepository.findByEspecialidad(tarifario.getEspecialidad());
        if (existente.isPresent()) {
            throw new IllegalStateException(
                    "Ya existe una tarifa para la especialidad: " + tarifario.getEspecialidad());
        }
        return tarifarioRepository.save(tarifario);
    }

    @Transactional
    public Tarifario actualizarTarifa(Integer id, Tarifario tarifaActualizada) {
        Tarifario tarifaExistente = buscarPorId(id);

        // Validar si la especialidad está siendo cambiada a una que ya existe
        if (!tarifaExistente.getEspecialidad().equalsIgnoreCase(tarifaActualizada.getEspecialidad())) {
            Optional<Tarifario> otraTarifa = tarifarioRepository
                    .findByEspecialidad(tarifaActualizada.getEspecialidad());
            if (otraTarifa.isPresent() && !otraTarifa.get().getId().equals(id)) {
                throw new IllegalStateException("La especialidad '" + tarifaActualizada.getEspecialidad()
                        + "' ya está asignada a otra tarifa.");
            }
        }

        tarifaExistente.setEspecialidad(tarifaActualizada.getEspecialidad());
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