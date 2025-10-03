package com.saludvida.api.controller;

import com.saludvida.api.model.Consulta;
import com.saludvida.api.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping("/historia/{idHistoriaClinica}")
    @PreAuthorize("hasAuthority('MEDICO')")
    public ResponseEntity<Consulta> registrarConsulta(@PathVariable Integer idHistoriaClinica, @RequestBody Consulta consulta) {
        return ResponseEntity.ok(consultaService.registrarConsulta(consulta, idHistoriaClinica));
    }
}
