package com.saludvida.api.service;

import com.saludvida.api.dto.CitaParaFacturacionDto;
import com.saludvida.api.model.Cita;
import com.saludvida.api.model.Paciente;
import com.saludvida.api.model.Tarifario;
import com.saludvida.api.repository.CitaRepository;
import com.saludvida.api.repository.PacienteRepository;
import com.saludvida.api.repository.TarifarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturacionService {
    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final TarifarioRepository tarifarioRepository;

    public List<CitaParaFacturacionDto> obtenerCitasPendientesPorDni(String dni) {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró paciente con DNI: " + dni));

        List<Cita> citas = citaRepository.findByPacienteIdPacienteAndEstadoPago(
                paciente.getIdPaciente(), Cita.EstadoPago.PENDIENTE);

        return citas.stream()
                .map(this::convertirACitaParaFacturacionDto)
                .collect(Collectors.toList());
    }

    private CitaParaFacturacionDto convertirACitaParaFacturacionDto(Cita cita) {
        String especialidad = cita.getMedico().getEspecialidad();
        BigDecimal precio = tarifarioRepository.findByEspecialidad(especialidad)
                .map(Tarifario::getPrecio)
                .orElse(BigDecimal.ZERO);

        return new CitaParaFacturacionDto(
                cita.getIdCita(),
                cita.getPaciente().getNombres(),
                cita.getPaciente().getApellidos(),
                cita.getPaciente().getDni(),
                especialidad,
                "Dr. " + cita.getMedico().getNombres() + " " + cita.getMedico().getApellidos(),
                cita.getFecha(),
                cita.getHora(),
                cita.getTieneSeguro(),
                precio,
                cita.getEstadoPago());
    }
}
