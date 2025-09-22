package com.saludvida.api.model;

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
@Table(name = "historiaclinica")
public class HistoriaClinica {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idHistoriaClinica")
    private Integer idHistoriaClinica;

    @Column(name = "fechaCreacion")
    private LocalDate fechaCreacion;

    @Column(name = "antecedentes")
    private String antecedentes;

    @Column(name = "alergias")
    private String alergias;

    @Column(name = "enfermedadesCronicas")
    private String enfermedadesCronicas;

    @OneToOne
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente")
    private Paciente paciente;
}
