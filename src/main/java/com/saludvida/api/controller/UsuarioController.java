package com.saludvida.api.controller;

import com.saludvida.api.dto.UsuarioRequestDto;
import com.saludvida.api.dto.UsuarioResponseDto;
import com.saludvida.api.model.Rol;
import com.saludvida.api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')") 
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> listarRoles() {
        return ResponseEntity.ok(usuarioService.listarRoles());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> crearUsuario(@RequestBody UsuarioRequestDto dto) {
        return ResponseEntity.ok(usuarioService.crearUsuario(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> actualizarUsuario(@PathVariable Integer id,
            @RequestBody UsuarioRequestDto dto) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, dto));
    }

    @PutMapping("/{id}/inactivar")
    public ResponseEntity<UsuarioResponseDto> inactivarUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.inactivarUsuario(id));
    }

}