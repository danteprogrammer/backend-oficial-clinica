package com.saludvida.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> checkHealth() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Backend funcionando correctamente");
        return ResponseEntity.ok(response);
    }
}