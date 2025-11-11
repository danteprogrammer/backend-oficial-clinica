package com.saludvida.api.service;

import com.saludvida.api.model.Especialidad;
import com.saludvida.api.repository.EspecialidadRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    @Transactional(readOnly = true)
    public List<Especialidad> listarEspecialidades() {
        return especialidadRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Especialidad buscarPorId(Integer id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Especialidad no encontrada con ID: " + id));
    }

    @Transactional
    public Especialidad crearEspecialidad(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    @Transactional
    public Especialidad actualizarEspecialidad(Integer id, Especialidad especialidadActualizada) {
        Especialidad existente = buscarPorId(id);
        existente.setNombre(especialidadActualizada.getNombre());
        existente.setDescripcion(especialidadActualizada.getDescripcion());
        return especialidadRepository.save(existente);
    }

    @Transactional
    public void eliminarEspecialidad(Integer id) {
        if (!especialidadRepository.existsById(id)) {
            throw new EntityNotFoundException("Especialidad no encontrada con ID: " + id);
        }
        especialidadRepository.deleteById(id);
    }
}