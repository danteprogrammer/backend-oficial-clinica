package com.saludvida.api.controller;

import com.saludvida.api.dto.OrdenLabRequestDto;
import com.saludvida.api.dto.OrdenLaboratorioResponseDto;
import com.saludvida.api.dto.ResultadosLabRequestDto;
import com.saludvida.api.model.OrdenLaboratorio;
import com.saludvida.api.service.LaboratorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/laboratorio")
@RequiredArgsConstructor
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    // --- Endpoints para MÉDICO ---

    @PostMapping("/ordenar")
    @PreAuthorize("hasAuthority('MEDICO')")
    public ResponseEntity<OrdenLaboratorio> crearOrden(@RequestBody OrdenLabRequestDto request) {
        return ResponseEntity.ok(laboratorioService.crearOrden(request));
    }

    @GetMapping("/historia/{idHistoria}")
    @PreAuthorize("hasAuthority('MEDICO')")
    public ResponseEntity<List<OrdenLaboratorio>> getOrdenesPorHistoria(@PathVariable Integer idHistoria) {
        return ResponseEntity.ok(laboratorioService.obtenerOrdenesPorHistoria(idHistoria));
    }

    // --- Endpoints para LABORATORIO ---

    // --- MÉTODO MODIFICADO ---
    @GetMapping("/pendientes")
    @PreAuthorize("hasAuthority('LABORATORIO')")
    public ResponseEntity<List<OrdenLaboratorioResponseDto>> getOrdenesPendientes() {
        return ResponseEntity.ok(laboratorioService.obtenerOrdenesPendientes());
    }

    @PutMapping("/{idOrden}/estado")
    @PreAuthorize("hasAuthority('LABORATORIO')")
    public ResponseEntity<OrdenLaboratorio> actualizarEstado(@PathVariable Integer idOrden,
            @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(laboratorioService.actualizarEstado(idOrden, nuevoEstado));
    }

    @PutMapping("/{idOrden}/resultados")
    @PreAuthorize("hasAuthority('LABORATORIO')")
    public ResponseEntity<OrdenLaboratorio> registrarResultados(@PathVariable Integer idOrden,
            @RequestBody ResultadosLabRequestDto request) {
        return ResponseEntity.ok(laboratorioService.registrarResultados(idOrden, request));
    }
}