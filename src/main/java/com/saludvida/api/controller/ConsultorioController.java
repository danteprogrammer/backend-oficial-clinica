package com.saludvida.api.controller;

import com.saludvida.api.model.Consultorio;
import com.saludvida.api.service.ConsultorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/consultorios")
@RequiredArgsConstructor
public class ConsultorioController {

    private final ConsultorioService consultorioService;

    // Endpoint para obtener todos los consultorios (GET /api/consultorios)
    // Se requiere el rol de 'ADMIN', 'RECEPCIONISTA' o 'MEDICO' para acceder a esta información
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCIONISTA', 'MEDICO')")
    public ResponseEntity<List<Consultorio>> obtenerTodosLosConsultorios() {
        return ResponseEntity.ok(consultorioService.obtenerTodos());
    }
}