package com.saludvida.api.service;

import com.saludvida.api.dto.PacienteUpdateDTO;
import com.saludvida.api.model.HistoriaClinica;
import com.saludvida.api.model.Paciente;
import com.saludvida.api.repository.HistoriaClinicaRepository;
import com.saludvida.api.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;

    @Transactional
    public Paciente registrarNuevoPaciente(Paciente paciente) {
        pacienteRepository.findByDni(paciente.getDni()).ifPresent(p -> {
            throw new IllegalStateException("Ya existe un paciente con el DNI proporcionado.");
        });

        paciente.setEstado(Paciente.Estado.Activo);
        Paciente nuevoPaciente = pacienteRepository.save(paciente);

        HistoriaClinica historia = HistoriaClinica.builder()
                .paciente(nuevoPaciente)
                .fechaCreacion(LocalDate.now())
                .build();
        historiaClinicaRepository.save(historia);

        return nuevoPaciente;
    }

    public Page<Paciente> buscarPacientesActivos(String termino, String filtro, Pageable pageable) {
        if ("DNI".equalsIgnoreCase(filtro)) {
            return pacienteRepository.findByEstadoAndDniContaining(Paciente.Estado.Activo, termino, pageable);
        }
        return pacienteRepository.findByEstadoAndNombresContainingIgnoreCaseOrEstadoAndApellidosContainingIgnoreCase(
                Paciente.Estado.Activo, termino, Paciente.Estado.Activo, termino, pageable);
    }

    public Page<Paciente> buscarPacientesInactivos(Pageable pageable) {
        return pacienteRepository.findByEstado(Paciente.Estado.Inactivo, pageable);
    }

    public Paciente cambiarEstado(Integer id, Paciente.Estado nuevoEstado) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con ID: " + id));
        paciente.setEstado(nuevoEstado);
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public Paciente modificarPaciente(Integer id, PacienteUpdateDTO datos) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con ID: " + id));

        paciente.setNombres(datos.getNombres());
        paciente.setApellidos(datos.getApellidos());
        paciente.setSexo(datos.getSexo());
        paciente.setFechaNacimiento(datos.getFechaNacimiento());
        paciente.setDireccion(datos.getDireccion());
        paciente.setTelefono(datos.getTelefono());
        paciente.setEmail(datos.getEmail());

        HistoriaClinica historia = historiaClinicaRepository.findByPaciente_IdPaciente(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Historia Clínica no encontrada para el paciente con ID: " + id));

        historia.setAlergias(datos.getAlergias());
        historia.setAntecedentes(datos.getAntecedentes());
        historia.setEnfermedadesCronicas(datos.getEnfermedadesCronicas());

        historiaClinicaRepository.save(historia);
        return pacienteRepository.save(paciente);
    }

    public PacienteUpdateDTO obtenerPacienteParaModificar(Integer id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con ID: " + id));

        HistoriaClinica historia = historiaClinicaRepository.findByPaciente_IdPaciente(id)
                .orElse(new HistoriaClinica()); 

        PacienteUpdateDTO dto = new PacienteUpdateDTO();
        dto.setNombres(paciente.getNombres());
        dto.setApellidos(paciente.getApellidos());
        dto.setSexo(paciente.getSexo());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setDireccion(paciente.getDireccion());
        dto.setTelefono(paciente.getTelefono());
        dto.setEmail(paciente.getEmail());

        dto.setAlergias(historia.getAlergias());
        dto.setAntecedentes(historia.getAntecedentes());
        dto.setEnfermedadesCronicas(historia.getEnfermedadesCronicas());

        return dto;
    }
}
