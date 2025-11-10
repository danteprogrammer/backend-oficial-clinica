package com.saludvida.api.controller;

import com.saludvida.api.model.Cita;
import com.saludvida.api.model.Cita.Estado;
import com.saludvida.api.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CitaController {

    private final CitaService citaService;

    @GetMapping
    public ResponseEntity<Page<Cita>> listarCitas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(citaService.listarCitas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCita(@PathVariable Integer id) {
        return ResponseEntity.ok(citaService.obtenerCitaPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cita> registrarCita(@RequestBody Cita cita) {
        Cita nuevaCita = citaService.registrarCita(cita);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Cita> actualizarEstado(@PathVariable Integer id, @RequestParam Estado estado) {
        return ResponseEntity.ok(citaService.actualizarEstado(id, estado));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Cita> cancelarCita(@PathVariable Integer id) {
        return ResponseEntity.ok(citaService.cancelarCita(id));
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<Cita> completarCita(@PathVariable Integer id) {
        return ResponseEntity.ok(citaService.completarCita(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<Page<Cita>> buscarPorPaciente(
            @PathVariable Integer pacienteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(citaService.buscarCitasPorPaciente(pacienteId, pageable));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<Page<Cita>> buscarPorFecha(
            @PathVariable String fecha,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                citaService.buscarCitasPorFecha(java.time.LocalDate.parse(fecha), pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizarCita(@PathVariable Integer id, @RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.actualizarCita(id, cita));
    }

    @GetMapping("/horas-disponibles")
    public ResponseEntity<List<String>> getHorasDisponibles(
            @RequestParam Integer idMedico,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<String> horas = citaService.getHorasDisponibles(idMedico, fecha);
        return ResponseEntity.ok(horas);
    }
}