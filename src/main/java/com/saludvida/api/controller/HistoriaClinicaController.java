package com.saludvida.api.controller;

import com.saludvida.api.model.HistoriaClinica;
import com.saludvida.api.repository.HistoriaClinicaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historias")
@RequiredArgsConstructor
public class HistoriaClinicaController {

    private final HistoriaClinicaRepository historiaClinicaRepository;

    @GetMapping("/paciente/{idPaciente}")
    @PreAuthorize("hasAnyAuthority('MEDICO', 'ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<HistoriaClinica> obtenerHistoriaPorPacienteId(@PathVariable Integer idPaciente) {
        HistoriaClinica historia = historiaClinicaRepository.findByPaciente_IdPaciente(idPaciente)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró historia clínica para el paciente con ID: " + idPaciente));
        return ResponseEntity.ok(historia);
    }
}
