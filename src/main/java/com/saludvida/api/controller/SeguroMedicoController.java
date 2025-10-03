package com.saludvida.api.controller;

import com.saludvida.api.dto.ValidacionSeguroResponse;
import com.saludvida.api.service.SeguroMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seguros")
@RequiredArgsConstructor
public class SeguroMedicoController {

    private final SeguroMedicoService seguroMedicoService;

    @GetMapping("/validar/paciente/{idPaciente}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ValidacionSeguroResponse> validarSeguro(@PathVariable Integer idPaciente) {
        ValidacionSeguroResponse response = seguroMedicoService.validarSeguroPorPacienteId(idPaciente);
        return ResponseEntity.ok(response);
    }
}
