package com.saludvida.api.controller;

import com.saludvida.api.model.Consultorio;
import com.saludvida.api.service.ConsultorioService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultorios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConsultorioController {

    private final ConsultorioService consultorioService;

    @GetMapping
    public ResponseEntity<List<Consultorio>> obtenerTodosLosConsultorios() {
        try {
            List<Consultorio> consultorios = consultorioService.obtenerTodos();
            return ResponseEntity.ok(consultorios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Consultorio>> obtenerConsultoriosDisponibles() {
        try {
            List<Consultorio> consultorios = consultorioService.obtenerConsultoriosDisponibles();
            return ResponseEntity.ok(consultorios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultorio> obtenerConsultorioPorId(@PathVariable Integer id) {
        try {
            return consultorioService.obtenerPorId(id)
                    .map(consultorio -> ResponseEntity.ok(consultorio))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Consultorio>> obtenerConsultoriosPorEstado(@PathVariable String estado) {
        try {
            Consultorio.Estado estadoEnum = Consultorio.Estado.valueOf(estado);
            List<Consultorio> consultorios = consultorioService.obtenerPorEstado(estadoEnum);
            return ResponseEntity.ok(consultorios);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoConsultorio(@PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        try {
            String nuevoEstado = request.get("estado");
            Consultorio.Estado estadoEnum = Consultorio.Estado.valueOf(nuevoEstado);

            Consultorio consultorioActualizado = consultorioService.actualizarEstado(id, estadoEnum);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estado del consultorio actualizado correctamente");
            response.put("data", consultorioActualizado);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return generarRespuestaError("Estado no válido: " + request.get("estado"), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return generarRespuestaError(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return generarRespuestaError("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> crearConsultorio(@RequestBody Consultorio consultorio) {
        try {
            Consultorio consultorioCreado = consultorioService.crearConsultorio(consultorio);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Consultorio creado correctamente");
            response.put("data", consultorioCreado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return generarRespuestaError(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return generarRespuestaError(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarConsultorio(@PathVariable Integer id, @RequestBody Consultorio consultorioData) {
        try {
            Consultorio consultorioActualizado = consultorioService.actualizarConsultorio(id, consultorioData);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Consultorio actualizado correctamente");
            response.put("data", consultorioActualizado);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return generarRespuestaError(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return generarRespuestaError("Error interno del servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarConsultorio(@PathVariable Integer id) {
        try {
            consultorioService.eliminarConsultorio(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Consultorio eliminado correctamente");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return generarRespuestaError(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return generarRespuestaError("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Map<String, Object>> generarRespuestaError(String mensaje, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", mensaje);
        return ResponseEntity.status(status).body(errorResponse);
    }
}