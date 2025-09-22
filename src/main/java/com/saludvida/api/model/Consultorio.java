package com.saludvida.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "consultorios")
public class Consultorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConsultorio;

    private String numero; // puede ser "101" o "A1"
    private Integer piso;
    private String descripcion;

    // getters y setters
}
