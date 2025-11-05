package com.saludvida.api.controller;

import com.saludvida.api.dto.TriajeDto;
import com.saludvida.api.model.Triaje;
import com.saludvida.api.model.Usuario;
import com.saludvida.api.service.TriajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/triajes")
public class TriajeController {

    @Autowired
    private TriajeService triajeService;

    @PostMapping("/historia/{idHistoriaClinica}")
    public ResponseEntity<Triaje> registrarTriaje(
            @PathVariable Long idHistoriaClinica,
            @RequestBody TriajeDto triajeDto,
            @AuthenticationPrincipal Usuario usuario) {

        Triaje triajeGuardado = triajeService.registrarTriaje(idHistoriaClinica, triajeDto, usuario);
        return ResponseEntity.ok(triajeGuardado);
    }

    @GetMapping("/historia/{idHistoriaClinica}")
    public ResponseEntity<List<Triaje>> getTriajesPorHistoria(@PathVariable Long idHistoriaClinica) {
        List<Triaje> triajes = triajeService.getTriajesPorHistoria(idHistoriaClinica);
        return ResponseEntity.ok(triajes);
    }
}