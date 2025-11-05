package com.saludvida.api.controller;

import com.saludvida.api.dto.CitaParaFacturacionDto;
import com.saludvida.api.model.Cita;
import com.saludvida.api.service.FacturacionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturacion")
@RequiredArgsConstructor
public class FacturacionController {
    private final FacturacionService facturacionService;

    @GetMapping("/citas-pendientes/{dni}")
    @PreAuthorize("hasAuthority('CAJA')")
    public ResponseEntity<?> getCitasPendientes(@PathVariable String dni) {
        try {
            List<CitaParaFacturacionDto> citas = facturacionService.obtenerCitasPendientesPorDni(dni);
            return ResponseEntity.ok(citas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/registrar-pago/{idCita}")
    @PreAuthorize("hasAuthority('CAJA')")
    public ResponseEntity<Cita> registrarPago(@PathVariable Integer idCita) {
        return ResponseEntity.ok(facturacionService.registrarPago(idCita));
    }

}
