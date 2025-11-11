package com.saludvida.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "especialidad")
@Getter
@Setter
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEspecialidad;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    // Nota: El plan mencionaba un 'estado'. Lo omito por ahora
    // para seguir el código exacto del plan, pero puedes añadirlo aquí si lo
    // necesitas.
    // @Column(name = "estado")
    // private String estado;
}