package com.saludvida.api.service;

import com.saludvida.api.dto.DashboardStatsDto;
import com.saludvida.api.dto.EspecialidadConteoDto;
import com.saludvida.api.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CitaRepository citaRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        LocalDate hoy = LocalDate.now();

        BigDecimal ingresosHoy = citaRepository.sumIngresosPorFecha(hoy);
        Long pacientesAtendidosHoy = citaRepository.countCitasPagadasPorFecha(hoy);
        List<EspecialidadConteoDto> consultasPorEspecialidad = citaRepository.countConsultasPorEspecialidadHoy(hoy);

        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setIngresosHoy(ingresosHoy);
        stats.setPacientesAtendidosHoy(pacientesAtendidosHoy);
        stats.setConsultasPorEspecialidadHoy(consultasPorEspecialidad);

        return stats;
    }
}