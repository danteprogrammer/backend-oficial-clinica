package com.saludvida.api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardStatsDto {
    private BigDecimal ingresosHoy;
    private Long pacientesAtendidosHoy;
    private List<EspecialidadConteoDto> consultasPorEspecialidadHoy;
}
