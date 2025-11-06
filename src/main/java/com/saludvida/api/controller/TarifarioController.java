package com.saludvida.api.controller;

import com.saludvida.api.model.Tarifario;
import com.saludvida.api.service.TarifarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tarifario")
@RequiredArgsConstructor
public class TarifarioController {

    private final TarifarioService tarifarioService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAJA')")
    public ResponseEntity<List<Tarifario>> listarTarifario() {
        return ResponseEntity.ok(tarifarioService.listarTodos());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> crearTarifa(@RequestBody Tarifario tarifario) {
        try {
            Tarifario nuevaTarifa = tarifarioService.crearTarifa(tarifario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarifa);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> actualizarTarifa(@PathVariable Integer id, @RequestBody Tarifario tarifario) {
        try {
            Tarifario tarifaActualizada = tarifarioService.actualizarTarifa(id, tarifario);
            return ResponseEntity.ok(tarifaActualizada);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> eliminarTarifa(@PathVariable Integer id) {
        try {
            tarifarioService.eliminarTarifa(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Tarifa eliminada correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}