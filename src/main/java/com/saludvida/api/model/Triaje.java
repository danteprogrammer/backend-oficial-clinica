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
    @JoinColumn(name = "id_usuario", nullable = false) 
    private Usuario registradoPor;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = true)
    private Double peso; 

    @Column(nullable = true)
    private Double altura; 

    @Column(nullable = true)
    private Double imc; 

    @Column(length = 10, nullable = true)
    private String presionArterial; 

    @Column(nullable = true)
    private Double temperatura; 

    @Column(nullable = true)
    private Double saturacionOxigeno; 

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
