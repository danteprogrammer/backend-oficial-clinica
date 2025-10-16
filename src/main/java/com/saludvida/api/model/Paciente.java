package com.saludvida.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "paciente")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPaciente")
    private Integer idPaciente;

    @NotBlank(message = "El DNI es obligatorio.")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos.")
    @Column(name = "dni", unique = true) // Asegura que el DNI sea único en la base de datos
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios.")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres.")
    @Column(name = "nombres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios.")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres.")
    @Column(name = "apellidos")
    private String apellidos;

    @NotNull(message = "El sexo es obligatorio.")
    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Sexo sexo;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "direccion")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Size(min = 9, max = 9, message = "El teléfono debe tener 9 dígitos.")
    @Column(name = "telefono")
    private String telefono;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email no es válido.")
    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    public enum Sexo {
        Masculino, Femenino
    }

    public enum Estado {
        Activo, Inactivo
    }
}
