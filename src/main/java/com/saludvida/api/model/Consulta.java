package com.saludvida.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConsulta;

    private LocalDate fechaConsulta;
    private String motivo;
    private String diagnostico;
    private String tratamiento;

    private Double peso; 
    private Double altura; 
    private Double imc;
    private String presionArterial;
    private Integer ritmoCardiaco;

    @ManyToOne
    @JoinColumn(name = "idMedico")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "idHistoriaClinica")
    @JsonBackReference
    private HistoriaClinica historiaClinica;
}
