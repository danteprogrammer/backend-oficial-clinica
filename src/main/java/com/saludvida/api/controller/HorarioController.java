package com.saludvida.api.controller;

import com.saludvida.api.dto.HorarioRequestDto;
import com.saludvida.api.model.Horario;
import com.saludvida.api.service.HorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/horarios")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')") // Proteger todo el controlador
public class HorarioController {

    private final HorarioService horarioService;

    @GetMapping("/{idMedico}")
    public ResponseEntity<List<Horario>> getHorariosPorMedico(@PathVariable Integer idMedico) {
        return ResponseEntity.ok(horarioService.getHorariosPorMedico(idMedico));
    }

    @PostMapping
    public ResponseEntity<Horario> crearHorario(@RequestBody HorarioRequestDto dto) {
        return ResponseEntity.ok(horarioService.crearHorario(dto));
    }

    @DeleteMapping("/{idHorario}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Integer idHorario) {
        horarioService.eliminarHorario(idHorario);
        return ResponseEntity.noContent().build();
    }
}