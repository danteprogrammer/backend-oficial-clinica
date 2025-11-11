package com.saludvida.api.controller;

import com.saludvida.api.model.Especialidad;
import com.saludvida.api.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/especialidades")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<Especialidad>> listarEspecialidades() {
        return ResponseEntity.ok(especialidadService.listarEspecialidades());
    }

    @PostMapping
    public ResponseEntity<Especialidad> crearEspecialidad(@RequestBody Especialidad especialidad) {
        return ResponseEntity.ok(especialidadService.crearEspecialidad(especialidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Especialidad> actualizarEspecialidad(@PathVariable Integer id, @RequestBody Especialidad especialidad) {
        return ResponseEntity.ok(especialidadService.actualizarEspecialidad(id, especialidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEspecialidad(@PathVariable Integer id) {
        especialidadService.eliminarEspecialidad(id);
        return ResponseEntity.noContent().build();
    }
}