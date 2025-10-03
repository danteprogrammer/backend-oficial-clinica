package com.saludvida.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seguromedico")
public class SeguroMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSeguro;

    private String nombreAseguradora;
    private String numeroPoliza;
    private String cobertura;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente")
    @JsonIgnore
    private Paciente paciente;
}
