package com.saludvida.api.dto;

import com.saludvida.api.model.Cita;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class CitaParaFacturacionDto {
    private Integer idCita;
    private Integer idPaciente;
    private String nombresPaciente;
    private String apellidosPaciente;
    private String dniPaciente;
    private String especialidad;
    private String medico;
    private String consultorioNumero; 
    private String consultorioDescripcion; 
    private LocalDate fecha;
    private LocalTime hora;
    private Boolean tieneSeguro;
    private BigDecimal precioConsulta;
    private Cita.EstadoPago estadoPago;
}
