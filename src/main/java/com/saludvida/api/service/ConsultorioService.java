package com.saludvida.api.service;

import com.saludvida.api.model.Consultorio;
import com.saludvida.api.repository.ConsultorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultorioService {

    private final ConsultorioRepository consultorioRepository;

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
        return consultorioRepository.save(consultorio);
    }

    @Transactional
    public void eliminarConsultorio(Integer id) {
        if (!consultorioRepository.existsById(id)) {
            throw new RuntimeException("Consultorio no encontrado con ID: " + id);
        }
        consultorioRepository.deleteById(id);
    }
}
