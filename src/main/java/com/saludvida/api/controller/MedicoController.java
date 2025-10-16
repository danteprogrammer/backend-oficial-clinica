package com.saludvida.api.controller;

import com.saludvida.api.model.Medico;
import com.saludvida.api.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicoController {

    private final MedicoService medicoService;

    /**
     * Obtiene todos los médicos
     */
    @GetMapping
    public ResponseEntity<List<Medico>> getMedicos() {
        try {
            List<Medico> medicos = medicoService.obtenerTodosLosMedicos();
            return ResponseEntity.ok(medicos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene un médico por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Medico> getMedico(@PathVariable Integer id) {
        try {
            Optional<Medico> medico = medicoService.obtenerMedicoPorId(id);
            return medico.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene médicos disponibles
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Medico>> getMedicosDisponibles() {
        try {
            List<Medico> medicos = medicoService.obtenerMedicosDisponibles();
            return ResponseEntity.ok(medicos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene médicos por especialidad
     */
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<Medico>> getMedicosPorEspecialidad(@PathVariable String especialidad) {
        try {
            List<Medico> medicos = medicoService.obtenerMedicosPorEspecialidad(especialidad);
            return ResponseEntity.ok(medicos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene médicos por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Medico>> getMedicosPorEstado(@PathVariable String estado) {
        try {
            Medico.Estado estadoEnum = Medico.Estado.valueOf(estado);
            List<Medico> medicos = medicoService.obtenerMedicosPorEstado(estadoEnum);
            return ResponseEntity.ok(medicos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crea un nuevo médico
     */
    @PostMapping
    public ResponseEntity<?> crearMedico(@RequestBody Medico medico) {
        try {
            Medico medicoCreado = medicoService.crearMedico(medico);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Médico creado correctamente");
            response.put("data", medicoCreado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Actualiza un médico existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMedico(@PathVariable Integer id, @RequestBody Medico medico) {
        try {
            Medico medicoActualizado = medicoService.actualizarMedico(id, medico);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Médico actualizado correctamente");
            response.put("data", medicoActualizado);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Elimina un médico
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedico(@PathVariable Integer id) {
        try {
            medicoService.eliminarMedico(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Médico eliminado correctamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Cambia el estado de un médico
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoMedico(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        try {
            String nuevoEstado = request.get("estado");
            Medico.Estado estado = Medico.Estado.valueOf(nuevoEstado);
            Medico medicoActualizado = medicoService.cambiarEstadoMedico(id, estado);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estado del médico actualizado correctamente");
            response.put("data", medicoActualizado);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Estado no válido: " + request.get("estado"));
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/especialidades")
    public ResponseEntity<List<String>> getEspecialidades() {
        try {
            List<String> especialidades = medicoService.obtenerEspecialidades();
            return ResponseEntity.ok(especialidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene el horario de un médico por ID (simulado)
     */
    @GetMapping("/{id}/horario")
    public ResponseEntity<Map<String, List<String>>> getHorarioMedico(@PathVariable Integer id) {
        try {
            Map<String, List<String>> horario = medicoService.obtenerHorarioMedico(id);
            return ResponseEntity.ok(horario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
