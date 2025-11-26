package com.saludvida.api.controller;

import com.saludvida.api.dto.DashboardStatsDto;
import com.saludvida.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DashboardStatsDto> getDashboardStats(
            @RequestParam(required = false) LocalDate fecha
    ) {
        return ResponseEntity.ok(dashboardService.getDashboardStats(fecha));
    }
}