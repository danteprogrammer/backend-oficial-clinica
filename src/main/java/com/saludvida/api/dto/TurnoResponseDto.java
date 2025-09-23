package com.saludvida.api.dto;

import com.saludvida.api.model.Turno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnoResponseDto {
    private Integer idTurno;
    private Integer idPaciente;
    private String nombresPaciente;
    private String apellidosPaciente;
    private String dniPaciente;
    private Integer idConsultorio;
    private String numeroConsultorio;
    private Integer pisoConsultorio;
    private String descripcionConsultorio;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String observaciones;
    private String estado;

    public static TurnoResponseDto fromEntity(Turno turno) {
        return TurnoResponseDto.builder()
                .idTurno(turno.getIdTurno())
                .idPaciente(turno.getPaciente().getIdPaciente())
                .nombresPaciente(turno.getPaciente().getNombres())
                .apellidosPaciente(turno.getPaciente().getApellidos())
                .dniPaciente(turno.getPaciente().getDni())
                .idConsultorio(turno.getConsultorio().getIdConsultorio())
                .numeroConsultorio(turno.getConsultorio().getNumero())
                .pisoConsultorio(turno.getConsultorio().getPiso())
                .descripcionConsultorio(turno.getConsultorio().getDescripcion())
                .fecha(turno.getFecha())
                .hora(turno.getHora())
                .motivo(turno.getMotivo())
                .observaciones(turno.getObservaciones())
                .estado(turno.getEstado().toString())
                .build();
    }
}
