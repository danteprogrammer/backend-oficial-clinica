package com.saludvida.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "triaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Triaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTriaje;

    @ManyToOne
    @JoinColumn(name = "id_historia_clinica", nullable = false)
    private HistoriaClinica historiaClinica;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false) // Para saber qué enfermero/a lo registró
    private Usuario registradoPor;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = true) // Hacemos los campos 'nullable' para flexibilidad
    private Double peso; // en kg

    @Column(nullable = true)
    private Double altura; // en cm

    @Column(nullable = true)
    private Double imc; // Índice de Masa Corporal (calculado)

    @Column(length = 10, nullable = true)
    private String presionArterial; // ej. "120/80"

    @Column(nullable = true)
    private Double temperatura; // en °C

    @Column(nullable = true)
    private Double saturacionOxigeno; // en %

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
