package com.saludvida.api.controller;

import com.saludvida.api.dto.TurnoDto;
import com.saludvida.api.model.Turno;
import com.saludvida.api.service.TurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    // Endpoint para registrar un nuevo turno (POST /api/turnos)
    // Se requiere el rol de 'ADMIN' o 'RECEPCIONISTA'
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Turno> registrarTurno(@RequestBody TurnoDto turnoDTO) {
        Turno nuevoTurno = turnoService.registrarTurno(turnoDTO);
        return ResponseEntity.ok(nuevoTurno);
    }

    // Endpoint para obtener los turnos próximos (GET /api/turnos/proximos)
    // Se requiere el rol de 'ADMIN' o 'RECEPCIONISTA'
    @GetMapping("/proximos")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<Turno>> obtenerTurnosProximos() {
        return ResponseEntity.ok(turnoService.obtenerTurnosProximos());
    }

    // Endpoint para cambiar el estado de un turno (PUT /api/turnos/{id}/estado)
    // Se requiere el rol de 'ADMIN' o 'RECEPCIONISTA'
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> cambiarEstadoTurno(@PathVariable Integer id, @RequestBody TurnoDto turnoDTO) {
        turnoService.cambiarEstadoTurno(id, turnoDTO.getEstado());
        return ResponseEntity.ok().build();
    }

    // Endpoint para cancelar un turno (PUT /api/turnos/{id}/cancelar)
    // Se requiere el rol de 'ADMIN' o 'RECEPCIONISTA'
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> cancelarTurno(@PathVariable Integer id) {
        turnoService.cancelarTurno(id);
        return ResponseEntity.ok().build();
    }
}