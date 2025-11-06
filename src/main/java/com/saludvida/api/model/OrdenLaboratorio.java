package com.saludvida.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orden_laboratorio")
public class OrdenLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrden;

    @ManyToOne
    @JoinColumn(name = "idHistoriaClinica")
    @JsonBackReference
    private HistoriaClinica historiaClinica;

    @ManyToOne
    @JoinColumn(name = "idMedico")
    private Medico medico;

    @Column(nullable = false)
    private LocalDate fechaOrden;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String examenesSolicitados; 

    @Column(columnDefinition = "TEXT")
    private String resultados;

    private LocalDate fechaResultados;

    @PrePersist
    protected void onCreate() {
        this.fechaOrden = LocalDate.now();
        this.estado = EstadoOrden.PENDIENTE;
    }

    public enum EstadoOrden {
        PENDIENTE, EN_PROCESO, COMPLETADO
    }
}
