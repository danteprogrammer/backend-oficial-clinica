package com.saludvida.api.controller;

import com.saludvida.api.dto.AuthResponse;
import com.saludvida.api.dto.CambioPasswordDto;
import com.saludvida.api.dto.LoginRequest;
import com.saludvida.api.service.AuthService;
import com.saludvida.api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService; 

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        try {
            usuarioService.procesarOlvidoPassword(email);
            return ResponseEntity.ok(Map.of("message", "Si el correo existe, se han enviado las instrucciones."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error en la solicitud"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody CambioPasswordDto dto) {
        try {
            usuarioService.cambiarPasswordConToken(dto.getToken(), dto.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload, java.security.Principal principal) {
        String newPassword = payload.get("newPassword");
        try {
            usuarioService.cambiarPasswordAutenticado(principal.getName(), newPassword);
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error al cambiar la contraseña."));
        }
    }
}