package com.saludvida.api.controller;

import com.saludvida.api.dto.DatosSeguroDto;
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

    @PostMapping("/validar/paciente/{idPaciente}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCIONISTA', 'CAJA')") // Añadir CAJA
    public ResponseEntity<ValidacionSeguroResponse> validarSeguro(
            @PathVariable Integer idPaciente,
            @RequestBody(required = false) DatosSeguroDto datosSeguro // Hacerlo opcional por si no se ingresan datos
    ) {
        ValidacionSeguroResponse response = seguroMedicoService.validarSeguroPorPacienteId(idPaciente, datosSeguro);
        return ResponseEntity.ok(response);
    }
}
