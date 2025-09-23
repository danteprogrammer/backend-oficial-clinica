package com.saludvida.api.controller;

import com.saludvida.api.dto.TurnoDto;
import com.saludvida.api.dto.TurnoResponseDto;
import com.saludvida.api.model.Turno;
import com.saludvida.api.service.TurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TurnoController {

    private final TurnoService turnoService;

    @GetMapping
    public ResponseEntity<List<TurnoResponseDto>> obtenerTodosLosTurnos() {
        try {
            List<TurnoResponseDto> turnos = turnoService.obtenerTodosLosTurnos();
            return ResponseEntity.ok(turnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<TurnoResponseDto>> obtenerTurnosPorPaciente(@PathVariable Integer pacienteId) {
        try {
            List<TurnoResponseDto> turnos = turnoService.obtenerTurnosPorPaciente(pacienteId);
            return ResponseEntity.ok(turnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/consultorio/{consultorioId}")
    public ResponseEntity<List<TurnoResponseDto>> obtenerTurnosPorConsultorio(@PathVariable Integer consultorioId) {
        try {
            List<TurnoResponseDto> turnos = turnoService.obtenerTurnosPorConsultorio(consultorioId);
            return ResponseEntity.ok(turnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TurnoResponseDto>> obtenerTurnosPorEstado(@PathVariable String estado) {
        try {
            Turno.Estado estadoEnum = Turno.Estado.valueOf(estado);
            List<TurnoResponseDto> turnos = turnoService.obtenerTurnosPorEstado(estadoEnum);
            return ResponseEntity.ok(turnos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<TurnoResponseDto>> obtenerTurnosPorFecha(@PathVariable String fecha) {
        try {
            LocalDate fechaLocal = LocalDate.parse(fecha);
            List<TurnoResponseDto> turnos = turnoService.obtenerTurnosPorFecha(fechaLocal);
            return ResponseEntity.ok(turnos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/asignar")
    public ResponseEntity<?> asignarTurno(@RequestBody TurnoDto turnoDto) {
        try {
            TurnoResponseDto turnoAsignado = turnoService.asignarTurno(turnoDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Turno asignado correctamente");
            response.put("data", turnoAsignado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoTurno(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        try {
            String nuevoEstado = request.get("estado");
            TurnoResponseDto turnoActualizado = turnoService.cambiarEstadoTurno(id, nuevoEstado);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estado del turno actualizado correctamente");
            response.put("data", turnoActualizado);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarTurno(@PathVariable Integer id) {
        try {
            turnoService.cancelarTurno(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Turno cancelado correctamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
