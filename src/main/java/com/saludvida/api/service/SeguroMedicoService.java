package com.saludvida.api.service;

import com.saludvida.api.dto.ValidacionSeguroResponse;
import com.saludvida.api.model.Paciente;
import com.saludvida.api.model.SeguroMedico;
import com.saludvida.api.repository.PacienteRepository;
import com.saludvida.api.repository.SeguroMedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeguroMedicoService {

    private final PacienteRepository pacienteRepository;
    private final SeguroMedicoRepository seguroMedicoRepository;

    public ValidacionSeguroResponse validarSeguroPorPacienteId(Integer idPaciente) {
        // 1. Busca al paciente
        Paciente paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con ID: " + idPaciente));

        // 2. Busca la información de su seguro
        SeguroMedico seguro = seguroMedicoRepository.findByPaciente_IdPaciente(idPaciente)
                .orElse(null);

        if (seguro == null) {
            return new ValidacionSeguroResponse("Rechazado", "El paciente no tiene un seguro registrado.", null);
        }

        // 3. Lógica de simulación: Válido si el último dígito del DNI es par
        try {
            String dni = paciente.getDni();
            int ultimoDigito = Integer.parseInt(dni.substring(dni.length() - 1));

            if (ultimoDigito % 2 == 0) {
                return new ValidacionSeguroResponse("Válido", "Cobertura del seguro activa.", seguro);
            } else {
                return new ValidacionSeguroResponse("Inválido",
                        "La póliza del seguro ha expirado o no tiene cobertura.", seguro);
            }
        } catch (Exception e) {
            return new ValidacionSeguroResponse("Error", "No se pudo validar el DNI del paciente.", seguro);
        }
    }
}
