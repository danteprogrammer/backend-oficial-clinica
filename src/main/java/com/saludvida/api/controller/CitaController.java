package com.saludvida.api.controller;

import com.saludvida.api.model.Cita;
import com.saludvida.api.model.Cita.Estado;
import com.saludvida.api.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @GetMapping
    public ResponseEntity<Page<Cita>> listarCitas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(citaService.listarCitas(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCita(@PathVariable Integer id) {
        return ResponseEntity.ok(citaService.obtenerCitaPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cita> registrarCita(@RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.registrarCita(cita));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Cita> actualizarEstado(@PathVariable Integer id, @RequestParam Estado estado) {
        return ResponseEntity.ok(citaService.actualizarEstado(id, estado));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Cita> cancelarCita(@PathVariable Integer id) {
        return ResponseEntity.ok(citaService.cancelarCita(id));
    }

    @PutMapping("/{id}/completar")
    public ResponseEntity<Cita> completarCita(@PathVariable Integer id) {
        return ResponseEntity.ok(citaService.completarCita(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<Page<Cita>> buscarPorPaciente(
            @PathVariable Integer pacienteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(citaService.buscarCitasPorPaciente(pacienteId, PageRequest.of(page, size)));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<Page<Cita>> buscarPorFecha(
            @PathVariable String fecha,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                citaService.buscarCitasPorFecha(java.time.LocalDate.parse(fecha), PageRequest.of(page, size))
        );
    }
}
