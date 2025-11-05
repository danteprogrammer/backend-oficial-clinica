package com.saludvida.api.controller;

import com.saludvida.api.dto.CitaParaFacturacionDto;
import com.saludvida.api.model.Cita;
import com.saludvida.api.service.FacturacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@RestController
@RequestMapping("/api/facturacion")
@RequiredArgsConstructor
public class FacturacionController {
    private final FacturacionService facturacionService;

    @GetMapping("/citas-pendientes/{dni}")
    @PreAuthorize("hasAuthority('CAJA')")
    public ResponseEntity<List<CitaParaFacturacionDto>> getCitasPendientes(@PathVariable String dni) {
        return ResponseEntity.ok(facturacionService.obtenerCitasPendientesPorDni(dni));
    }

    @PutMapping("/registrar-pago/{idCita}")
    @PreAuthorize("hasAuthority('CAJA')")
    public ResponseEntity<Cita> registrarPago(@PathVariable Integer idCita) {
        return ResponseEntity.ok(facturacionService.registrarPago(idCita));
    }

}
