package com.saludvida.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // <-- AÑADIDO
@NoArgsConstructor // <-- AÑADIDO
@AllArgsConstructor // <-- AÑADIDO
@Entity
@Table(name = "horarios")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Integer idHorario;

    // Lo mantenemos como String para flexibilidad (Lunes, Martes, etc.)
    @Column(name = "dia_semana", nullable = false)
    private String diaSemana;

    // Lo mantenemos como String para formato "HH:mm"
    @Column(name = "hora_inicio", nullable = false)
    private String horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private String horaFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", nullable = false)
    @JsonBackReference // Evita bucles infinitos al serializar
    private Medico medico;
}