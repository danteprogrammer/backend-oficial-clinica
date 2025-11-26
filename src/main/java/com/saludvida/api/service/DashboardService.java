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
    public DashboardStatsDto getDashboardStats(LocalDate fechaConsulta) {
        LocalDate fecha = (fechaConsulta != null) ? fechaConsulta : LocalDate.now();

        BigDecimal ingresosHoy = citaRepository.sumIngresosPorFecha(fecha);
        Long pacientesAtendidosHoy = citaRepository.countCitasPagadasPorFecha(fecha);
        List<EspecialidadConteoDto> consultasPorEspecialidad = citaRepository.countConsultasPorEspecialidadHoy(fecha);

        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setIngresosHoy(ingresosHoy != null ? ingresosHoy : BigDecimal.ZERO);
        stats.setPacientesAtendidosHoy(pacientesAtendidosHoy);
        stats.setConsultasPorEspecialidadHoy(consultasPorEspecialidad);

        return stats;
    }
}